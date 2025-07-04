package mg.biblio.gestion_biblio.repository;

import mg.biblio.gestion_biblio.model.ParametrePret;
import mg.biblio.gestion_biblio.model.TypeAdherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParametrePretRepository extends JpaRepository<ParametrePret, Long> {

    /**
     * Trouve les paramètres de prêt associés à un type d'adhérent spécifique.
     * 
     * @param typeAdherent L'entité TypeAdherent pour laquelle on cherche les
     *                     paramètres.
     * @return Un Optional contenant les ParametrePret s'ils existent.
     */
    Optional<ParametrePret> findByTypeAdherent(TypeAdherent typeAdherent);
}