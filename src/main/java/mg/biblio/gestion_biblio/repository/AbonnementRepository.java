package mg.biblio.gestion_biblio.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mg.biblio.gestion_biblio.model.Abonnement;

@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {

        /**
         * Trouve un abonnement actif pour un adhérent donné.
         * Un abonnement est considéré comme actif si sa date de fin est dans le futur
         * ou si la date de fin est nulle (cas d'un abonnement permanent).
         * 
         * @param adherentId   L'ID de l'adhérent.
         * @param dateActuelle La date du jour pour la comparaison.
         * @return Un Optional contenant l'Abonnement actif s'il existe.
         */

        @Query("SELECT ab FROM Abonnement ab WHERE ab.adherent.id = :adherentId " +
                        "AND (ab.dateFin IS NULL OR ab.dateFin >= :dateActuelle)")
        Optional<Abonnement> findActiveByAdherent(@Param("adherentId") Long adherentId,
                        @Param("dateActuelle") LocalDate dateActuelle);
}