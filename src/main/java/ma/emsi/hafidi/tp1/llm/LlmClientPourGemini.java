package ma.emsi.hafidi.tp1.llm;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class LlmClientPourGemini {
    private final Client clientRest = ClientBuilder.newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    private final WebTarget target = clientRest.target(
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent");

    public Response envoyerRequete(Entity<String> requete) {
        String cle = System.getenv("GEMINI_KEY");
        if (cle == null || cle.isBlank()) {
            throw new IllegalStateException("GEMINI_KEY est absente. Configurez-la et redémarrez Payara.");
        }
        return target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("x-goog-api-key", cle)
                .post(requete);
    }

    @PreDestroy
    public void fermer() {
        clientRest.close();
    }
}
