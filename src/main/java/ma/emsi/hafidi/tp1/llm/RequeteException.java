package ma.emsi.hafidi.tp1.llm;

public class RequeteException extends Exception {
    private final String requeteJson;
    private final String reponseJson;

    public RequeteException(String message, String requeteJson, String reponseJson) {
        super(message);
        this.requeteJson = requeteJson;
        this.reponseJson = reponseJson;
    }

    public String getRequeteJson() {
        return requeteJson;
    }

    public String getReponseJson() {
        return reponseJson;
    }
}
