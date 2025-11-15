package model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class OffreAppel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_client")
    private String emailClient;

    @Column(name = "nom_client")
    private String nomClient;

    @Column(name = "objet_appel")
    private String objetAppel;

    @Column(name = "date_soumission")
    private LocalDateTime dateSoumission;

    public OffreAppel() {}

    public OffreAppel(String emailClient, String nomClient, String objetAppel) {
        this.emailClient = emailClient;
        this.nomClient = nomClient;
        this.objetAppel = objetAppel;
        this.dateSoumission = LocalDateTime.now();
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getObjetAppel() {
        return objetAppel;
    }

    public void setObjetAppel(String objetAppel) {
        this.objetAppel = objetAppel;
    }

    public LocalDateTime getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(LocalDateTime dateSoumission) {
        this.dateSoumission = dateSoumission;
    }
}
