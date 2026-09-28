package RomainParedes.sport_team_manager;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/joueurs")
public class JoueurController {

    private final JoueurRepository joueurRepository;

    public JoueurController(JoueurRepository joueurRepository) {
        this.joueurRepository = joueurRepository;
    }

    @GetMapping
    public List<Joueur> getAll() {
        return joueurRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Joueur> getById(@PathVariable Long id) {
        return joueurRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Joueur create(@RequestBody Joueur joueur) {
        joueur.setId(null);
        return joueurRepository.save(joueur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Joueur> update(@PathVariable Long id, @RequestBody Joueur donnees) {
        return joueurRepository.findById(id).map(existant -> {
            existant.setNom(donnees.getNom());
            existant.setPrenom(donnees.getPrenom());
            existant.setPoste(donnees.getPoste());
            existant.setNumero(donnees.getNumero());
            existant.setActif(donnees.isActif());
            return ResponseEntity.ok(joueurRepository.save(existant));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!joueurRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        joueurRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}