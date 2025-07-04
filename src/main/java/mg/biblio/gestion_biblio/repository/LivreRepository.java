package mg.biblio.gestion_biblio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mg.biblio.gestion_biblio.model.Livre;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {

    /**
     * Récupère tous les livres en chargeant immédiatement (EAGER) leurs listes
     * d'exemplaires.
     * Utilise un 'LEFT JOIN FETCH' pour résoudre le problème de N+1 requêtes.
     * Sans cette requête, si vous avez 100 livres, Spring ferait 101 requêtes à la
     * BDD
     * pour afficher la liste (1 pour les livres, 100 pour les exemplaires de chaque
     * livre).
     * Avec cette requête, tout est fait en une seule fois.
     * 'DISTINCT' est utilisé pour éviter les doublons de livres dans le résultat
     * si un livre a plusieurs exemplaires.
     * 
     * @return Une liste de tous les livres, avec leurs exemplaires pré-chargés.
     */
    @Query("SELECT DISTINCT l FROM Livre l LEFT JOIN FETCH l.exemplaires")
    List<Livre> findAllWithExemplaires(); // Correction: Pas de corps de méthode, pas de 'static'
}