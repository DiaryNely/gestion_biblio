package mg.biblio.gestion_biblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mg.biblio.gestion_biblio.enums.TypeStatutPaiement;
import mg.biblio.gestion_biblio.model.Adherent;
import mg.biblio.gestion_biblio.model.Penalite;

@Repository
public interface PenaliteRepository extends JpaRepository<Penalite, Long> {

    /**
     * Compte le nombre de pénalités pour un adhérent qui ont un statut spécifique.
     * Typiquement utilisé pour vérifier les pénalités 'en_attente'.
     * 
     * @param adherent L'entité Adherent concernée.
     * @param statut   Le statut de paiement à rechercher (ex:
     *                 TypeStatutPaiement.en_attente).
     * @return Le nombre de pénalités correspondantes.
     */
    long countByAdherentAndStatut(Adherent adherent, TypeStatutPaiement statut);
}
