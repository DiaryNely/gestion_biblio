package mg.biblio.gestion_biblio.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository; // Nous allons créer ce DTO
import org.springframework.data.jpa.repository.Query;

import mg.biblio.gestion_biblio.dto.LivreEmpruntStatsDTO;
import mg.biblio.gestion_biblio.model.Adherent;
import mg.biblio.gestion_biblio.model.Pret;

public interface PretRepository extends JpaRepository<Pret, Long> {

    /**
     * Compte le nombre de livres actuellement empruntés par un adhérent
     * (c'est-à-dire les prêts pour lesquels la date de retour réelle est nulle).
     * 
     * @param adherent L'adhérent concerné.
     * @return Le nombre de prêts en cours.
     */
    long countByAdherentAndDateRetourReelleIsNull(Adherent adherent);

    // Compte les prêts qui ne sont pas encore retournés
    long countByDateRetourReelleIsNull();

    // Compte les prêts non retournés ET dont la date de retour prévue est dépassée
    long countByDateRetourReelleIsNullAndDateRetourPrevueBefore(LocalDate date);

    // Requête JPQL pour trouver les 5 livres les plus empruntés
    @Query("SELECT new mg.biblio.gestion_biblio.dto.LivreEmpruntStatsDTO(p.exemplaire.livre.titre, COUNT(p.exemplaire.livre)) "
            +
            "FROM Pret p " +
            "GROUP BY p.exemplaire.livre.titre " +
            "ORDER BY COUNT(p.exemplaire.livre) DESC " +
            "LIMIT 5")
    List<LivreEmpruntStatsDTO> findTop5MostLoanedBooks();
}