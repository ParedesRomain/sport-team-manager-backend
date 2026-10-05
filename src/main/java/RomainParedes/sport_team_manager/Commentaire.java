package RomainParedes.sport_team_manager;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentaireId;

    private String contenu;
    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne
    private Joueur joueur;

    public Commentaire() {
    }

    public Long getCommentaireId() { return commentaireId; }
    public void setCommentaireId(Long commentaireId) { this.commentaireId = commentaireId; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Joueur getJoueur() { return joueur; }
    public void setJoueur(Joueur joueur) { this.joueur = joueur; }
}