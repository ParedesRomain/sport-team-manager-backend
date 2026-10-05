package RomainParedes.sport_team_manager;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findByRencontreRencontreId(Long rencontreId);
    boolean existsByRencontreRencontreIdAndParticipantJoueurId(Long rencontreId, Long joueurId);
}