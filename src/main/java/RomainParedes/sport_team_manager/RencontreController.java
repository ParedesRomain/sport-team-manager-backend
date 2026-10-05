package RomainParedes.sport_team_manager;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rencontres")
public class RencontreController {

    private final RencontreRepository rencontreRepository;

    public RencontreController(RencontreRepository rencontreRepository) {
        this.rencontreRepository = rencontreRepository;
    }

    @GetMapping
    public List<Rencontre> getAll() {
        return rencontreRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rencontre> getById(@PathVariable Long id) {
        return rencontreRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rencontre create(@RequestBody Rencontre rencontre) {
        rencontre.setRencontreId(null);
        return rencontreRepository.save(rencontre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rencontre> update(@PathVariable Long id, @RequestBody Rencontre donnees) {
        return rencontreRepository.findById(id).map(existant -> {
            existant.setDateHeure(donnees.getDateHeure());
            existant.setEquipeAdverse(donnees.getEquipeAdverse());
            existant.setAdresse(donnees.getAdresse());
            existant.setLieu(donnees.getLieu());
            return ResponseEntity.ok(rencontreRepository.save(existant));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Rencontre> enregistrerResultat(@PathVariable Long id, @RequestBody Rencontre donnees) {
        return rencontreRepository.findById(id).map(existant -> {
            existant.setResultat(donnees.getResultat());
            return ResponseEntity.ok(rencontreRepository.save(existant));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!rencontreRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rencontreRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}