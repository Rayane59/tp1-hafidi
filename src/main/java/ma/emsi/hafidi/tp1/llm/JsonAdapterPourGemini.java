package ma.emsi.hafidi.tp1.llm;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;
import jakarta.json.stream.JsonGenerator;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Dependent
public class JsonAdapterPourGemini implements Serializable {
    @Inject
    private LlmClientPourGemini geminiClient;

    private String systemRole;
    // Le JSON est stocké sous forme de texte pour rester sérialisable dans la vue JSF.
    private String historique;

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
    }

    public LlmInteraction envoyerRequete(String question) throws RequeteException {
        JsonObject requete = historique == null
                ? creerRequeteJson(question)
                : ajouterMessage(lireObjet(historique), messageUtilisateur(question));
        String texteRequete = mettreEnForme(requete);

        try (Response response = geminiClient.envoyerRequete(Entity.entity(requete.toString(),
                MediaType.APPLICATION_JSON_TYPE))) {
            String corps = response.readEntity(String.class);
            if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                throw new RequeteException("Gemini HTTP " + response.getStatus() + " : " + lireErreur(corps),
                        texteRequete, corps);
            }

            JsonObject objet = lireObjet(corps);
            JsonArray candidats = objet.getJsonArray("candidates");
            if (candidats == null || candidats.isEmpty()) {
                throw new RequeteException("Gemini n'a retourné aucune réponse.", texteRequete, corps);
            }

            JsonObject candidat = candidats.getJsonObject(0);
            JsonObject message = candidat.getJsonObject("content");
            String reponse = extraireTexte(message);
            if (reponse.isBlank()) {
                throw new RequeteException("Gemini n'a retourné aucun texte ("
                        + candidat.getString("finishReason", "motif inconnu") + ").", texteRequete, corps);
            }

            // Seuls les échanges réussis sont réenvoyés au tour suivant.
            historique = ajouterMessage(requete, message).toString();
            return new LlmInteraction(texteRequete, mettreEnForme(objet), reponse);
        } catch (ProcessingException e) {
            throw new RequeteException("Impossible de joindre Gemini.", texteRequete, null);
        } catch (IllegalStateException e) {
            throw new RequeteException(e.getMessage(), texteRequete, null);
        } catch (JsonException | ClassCastException e) {
            throw new RequeteException("La réponse de Gemini n'a pas le format JSON attendu.", texteRequete, null);
        }
    }

    private JsonObject creerRequeteJson(String question) {
        return Json.createObjectBuilder()
                .add("system_instruction", Json.createObjectBuilder().add("parts", partiesTexte(systemRole)))
                .add("contents", Json.createArrayBuilder().add(messageUtilisateur(question)))
                .build();
    }

    private static JsonObject ajouterMessage(JsonObject requete, JsonObject message) {
        return Json.createPointer("/contents/-").add(requete, message);
    }

    private static JsonObject messageUtilisateur(String texte) {
        return Json.createObjectBuilder().add("role", "user").add("parts", partiesTexte(texte)).build();
    }

    private static JsonArray partiesTexte(String texte) {
        return Json.createArrayBuilder().add(Json.createObjectBuilder().add("text", texte)).build();
    }

    private static String extraireTexte(JsonObject message) {
        JsonArray parties = message == null ? null : message.getJsonArray("parts");
        if (parties == null) {
            return "";
        }
        StringBuilder texte = new StringBuilder();
        for (JsonValue part : parties) {
            JsonObject objet = part.asJsonObject();
            if (!objet.getBoolean("thought", false)) {
                texte.append(objet.getString("text", ""));
            }
        }
        return texte.toString();
    }

    private static String lireErreur(String corps) {
        try {
            JsonObject erreur = lireObjet(corps).getJsonObject("error");
            if (erreur != null) {
                return erreur.getString("message", "requête refusée");
            }
        } catch (JsonException | ClassCastException e) {
            // Une passerelle HTTP peut renvoyer une page HTML.
        }
        return "requête refusée";
    }

    private static JsonObject lireObjet(String json) {
        try (JsonReader lecteur = Json.createReader(new StringReader(json))) {
            return lecteur.readObject();
        }
    }

    private static String mettreEnForme(JsonObject objet) {
        StringWriter texte = new StringWriter();
        try (JsonWriter writer = Json.createWriterFactory(Map.of(JsonGenerator.PRETTY_PRINTING, true))
                .createWriter(texte)) {
            writer.writeObject(objet);
        }
        return texte.toString();
    }
}
