package RomainParedes.sport_team_manager;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Joueur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joueurId;

    private String nom;
    private String prenom;
    private String numeroLicence;
    private LocalDate dateNaissance;
    private Integer taille;
    private Integer poids;

    @Enumerated(EnumType.STRING)
    private JoueurStatut statut = JoueurStatut.ACTIF;

    public Joueur() {
    }

    public Long getJoueurId() { return joueurId; }
    public void setJoueurId(Long joueurId) { this.joueurId = joueurId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNumeroLicence() { return numeroLicence; }
    public void setNumeroLicence(String numeroLicence) { this.numeroLicence = numeroLicence; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public Integer getTaille() { return taille; }
    public void setTaille(Integer taille) { this.taille = taille; }

    public Integer getPoids() { return poids; }
    public void setPoids(Integer poids) { this.poids = poids; }

    public JoueurStatut getStatut() { return statut; }
    public void setStatut(JoueurStatut statut) { this.statut = statut; }
}