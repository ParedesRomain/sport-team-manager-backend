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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participations")
public class ParticipationController {

    private final ParticipationRepository participationRepository;
    private final JoueurRepository joueurRepository;
    private final RencontreRepository rencontreRepository;

    public ParticipationController(ParticipationRepository participationRepository,
                                    JoueurRepository joueurRepository,
                                    RencontreRepository rencontreRepository) {
        this.participationRepository = participationRepository;
        this.joueurRepository = joueurRepository;
        this.rencontreRepository = rencontreRepository;
    }

    @GetMapping
    public List<Participation> getAll() {
        return participationRepository.findAll();
    }

    @GetMapping("/match/{rencontreId}")
    public List<Participation> getFeuilleDeMatch(@PathVariable Long rencontreId) {
        return participationRepository.findByRencontreRencontreId(rencontreId);
    }

    @GetMapping("/match/{rencontreId}/joueur/{joueurId}")
    public boolean estDejaSurLaFeuilleDeMatch(@PathVariable Long rencontreId, @PathVariable Long joueurId) {
        return participationRepository.existsByRencontreRencontreIdAndParticipantJoueurId(rencontreId, joueurId);
    }

    @GetMapping("/match/{rencontreId}/complete")
    public boolean estComplete(@PathVariable Long rencontreId) {
        List<Participation> participations = participationRepository.findByRencontreRencontreId(rencontreId);

        for (Poste poste : Poste.values()) {
            boolean titulairePresent = participations.stream().anyMatch(p ->
                    p.getPoste() == poste && p.getTitulaireOuRemplacant() == TitulaireOuRemplacant.TITULAIRE);
            if (!titulairePresent) {
                return false;
            }
        }

        return participations.stream().allMatch(p -> p.getParticipant().getStatut() == JoueurStatut.ACTIF);
    }

    public static class ParticipationRequest {
        public Long joueurId;
        public Long rencontreId;
        public Poste poste;
        public TitulaireOuRemplacant titulaireOuRemplacant;
    }

    @PostMapping
    public ResponseEntity<Participation> create(@RequestBody ParticipationRequest requete) {
        Joueur joueur = joueurRepository.findById(requete.joueurId).orElse(null);
        Rencontre rencontre = rencontreRepository.findById(requete.rencontreId).orElse(null);
        if (joueur == null || rencontre == null || requete.poste == null || requete.titulaireOuRemplacant == null) {
            return ResponseEntity.badRequest().build();
        }

        if (joueur.getStatut() != JoueurStatut.ACTIF) {
            return ResponseEntity.badRequest().build();
        }

        if (participationRepository.existsByRencontreRencontreIdAndParticipantJoueurId(
                requete.rencontreId, requete.joueurId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (participationRepository.existsByRencontreRencontreIdAndPosteAndTitulaireOuRemplacant(
                requete.rencontreId, requete.poste, requete.titulaireOuRemplacant)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Participation participation = new Participation();
        participation.setParticipant(joueur);
        participation.setRencontre(rencontre);
        participation.setPoste(requete.poste);
        participation.setTitulaireOuRemplacant(requete.titulaireOuRemplacant);

        return ResponseEntity.status(HttpStatus.CREATED).body(participationRepository.save(participation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participation> update(@PathVariable Long id, @RequestBody ParticipationRequest requete) {
        Participation existant = participationRepository.findById(id).orElse(null);
        if (existant == null) {
            return ResponseEntity.notFound().build();
        }

        Joueur joueur = joueurRepository.findById(requete.joueurId).orElse(null);
        if (joueur == null || requete.poste == null || requete.titulaireOuRemplacant == null) {
            return ResponseEntity.badRequest().build();
        }

        Long rencontreId = existant.getRencontre().getRencontreId();

        boolean joueurChange = !joueur.getJoueurId().equals(existant.getParticipant().getJoueurId());
        if (joueurChange) {
            if (joueur.getStatut() != JoueurStatut.ACTIF) {
                return ResponseEntity.badRequest().build();
            }
            if (participationRepository.existsByRencontreRencontreIdAndParticipantJoueurId(
                    rencontreId, joueur.getJoueurId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        boolean emplacementChange = existant.getPoste() != requete.poste
                || existant.getTitulaireOuRemplacant() != requete.titulaireOuRemplacant;
        if (emplacementChange && participationRepository.existsByRencontreRencontreIdAndPosteAndTitulaireOuRemplacant(
                rencontreId, requete.poste, requete.titulaireOuRemplacant)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        existant.setParticipant(joueur);
        existant.setPoste(requete.poste);
        existant.setTitulaireOuRemplacant(requete.titulaireOuRemplacant);
        return ResponseEntity.ok(participationRepository.save(existant));
    }

    public static class PerformanceRequest {
        public Performance performance;
    }

    @PatchMapping("/{id}/performance")
    public ResponseEntity<Participation> mettreAJourLaPerformance(@PathVariable Long id,
                                                                   @RequestBody PerformanceRequest requete) {
        return participationRepository.findById(id).map(existant -> {
            existant.setPerformance(requete.performance);
            return ResponseEntity.ok(participationRepository.save(existant));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/performance")
    public ResponseEntity<Participation> supprimerLaPerformance(@PathVariable Long id) {
        return participationRepository.findById(id).map(existant -> {
            existant.setPerformance(null);
            return ResponseEntity.ok(participationRepository.save(existant));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!participationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        participationRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}