package mg.biblio.gestion_biblio.repository;

import mg.biblio.gestion_biblio.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // Spring Data JPA va automatiquement comprendre cette méthode et générer la
    // requête :
    // "SELECT * FROM utilisateur WHERE email = ?"
    Optional<Utilisateur> findByEmail(String email);
}