package model;
public enum Role {
    ADMIN("ADMIN"),
    UTILISATEUR_RH("UTILISATEUR_RH"),
    UTILISATEUR_COMMERCIAL("UTILISATEUR_COMMERCIAL"),
    USER("USER");
    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
