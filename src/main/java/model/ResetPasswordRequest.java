package model;

public class ResetPasswordRequest {
    private String email;

    public ResetPasswordRequest() {
        // Constructeur par défaut requis par Jackson pour la désérialisation JSON
    }

    public ResetPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
