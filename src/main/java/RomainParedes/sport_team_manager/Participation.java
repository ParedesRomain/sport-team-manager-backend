package RomainParedes.sport_team_manager;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participationId;

    @ManyToOne
    private Joueur participant;

    @ManyToOne
    private Rencontre rencontre;

    @Enumerated(EnumType.STRING)
    private Poste poste;

    @Enumerated(EnumType.STRING)
    private TitulaireOuRemplacant titulaireOuRemplacant;

    @Enumerated(EnumType.STRING)
    private Performance performance;

    public Participation() {
    }

    public Long getParticipationId() { return participationId; }
    public void setParticipationId(Long participationId) { this.participationId = participationId; }

    public Joueur getParticipant() { return participant; }
    public void setParticipant(Joueur participant) { this.participant = participant; }

    public Rencontre getRencontre() { return rencontre; }
    public void setRencontre(Rencontre rencontre) { this.rencontre = rencontre; }

    public Poste getPoste() { return poste; }
    public void setPoste(Poste poste) { this.poste = poste; }

    public TitulaireOuRemplacant getTitulaireOuRemplacant() { return titulaireOuRemplacant; }
    public void setTitulaireOuRemplacant(TitulaireOuRemplacant titulaireOuRemplacant) { this.titulaireOuRemplacant = titulaireOuRemplacant; }

    public Performance getPerformance() { return performance; }
    public void setPerformance(Performance performance) { this.performance = performance; }
}