package mg.biblio.gestion_biblio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mg.biblio.gestion_biblio.model.Adherent;

@Repository
public interface AdherentRepository extends JpaRepository<Adherent, Long> {
    // Ici aussi, on a déjà count()

    /**
     * Trouve un adhérent en se basant sur l'email de l'entité Utilisateur associée.
     * C'est essentiel pour retrouver l'adhérent à partir de l'utilisateur
     * authentifié.
     * 
     * @param email L'email de l'utilisateur.
     * @return Un Optional contenant l'Adherent s'il est trouvé.
     */

    @Query("SELECT a FROM Adherent a JOIN a.utilisateur u WHERE u.email = :email")
    Optional<Adherent> findByUtilisateurEmail(@Param("email") String email);
}
