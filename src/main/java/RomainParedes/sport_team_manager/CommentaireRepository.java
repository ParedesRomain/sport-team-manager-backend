package RomainParedes.sport_team_manager;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    List<Commentaire> findByJoueurJoueurId(Long joueurId);
}