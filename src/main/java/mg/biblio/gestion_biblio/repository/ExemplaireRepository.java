package mg.biblio.gestion_biblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.biblio.gestion_biblio.enums.TypeEtatPhysique;
import mg.biblio.gestion_biblio.model.Exemplaire;

public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
    // Compte les exemplaires selon leur état de disponibilité
    long countByDisponible(boolean disponible);

    // Compte les exemplaires selon leur état physique
    long countByEtatPhysique(TypeEtatPhysique etat);
}