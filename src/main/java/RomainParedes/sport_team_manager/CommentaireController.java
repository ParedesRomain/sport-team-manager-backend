package RomainParedes.sport_team_manager;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commentaires")
public class CommentaireController {

    private final CommentaireRepository commentaireRepository;
    private final JoueurRepository joueurRepository;

    public CommentaireController(CommentaireRepository commentaireRepository, JoueurRepository joueurRepository) {
        this.commentaireRepository = commentaireRepository;
        this.joueurRepository = joueurRepository;
    }

    public static class CommentaireRequest {
        public String contenu;
        public Long joueurId;
    }

    @PostMapping
    public ResponseEntity<Commentaire> create(@RequestBody CommentaireRequest requete) {
        Joueur joueur = joueurRepository.findById(requete.joueurId).orElse(null);
        if (joueur == null) {
            return ResponseEntity.badRequest().build();
        }

        Commentaire commentaire = new Commentaire();
        commentaire.setContenu(requete.contenu);
        commentaire.setJoueur(joueur);

        return ResponseEntity.status(HttpStatus.OK).body(commentaireRepository.save(commentaire));
    }

    @GetMapping("/joueur/{joueurId}")
    public List<Commentaire> listerParJoueur(@PathVariable Long joueurId) {
        return commentaireRepository.findByJoueurJoueurId(joueurId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!commentaireRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        commentaireRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}