package mg.biblio.gestion_biblio.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "utilisateur")
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_utilisateur;

    private String nom;
    private String prenom;

    @Column(unique = true)
    private String email;

    @Column(name = "mot_de_passe_hash")
    private String motDePasseHash;

    // On utilise la classe String pour le type ENUM pour plus de simplicité ici.
    // Pour une meilleure pratique, on pourrait utiliser
    // @Enumerated(EnumType.STRING)
    private String statut;

    // --- Méthodes de l'interface UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Pour l'instant, on donne un rôle simple à tout le monde.
        // Plus tard, on pourra créer une table de rôles.
        // Le nom doit commencer par "ROLE_"
        if ("Admin".equals(this.nom)) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        // Retourne le champ qui contient le mot de passe hashé
        return this.motDePasseHash;
    }

    @Override
    public String getUsername() {
        // Retourne le champ utilisé pour l'identification (l'email dans notre cas)
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // On considère que les comptes n'expirent pas
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // On ne gère pas le verrouillage de compte pour l'instant
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Les identifiants n'expirent pas
    }

    @Override
    public boolean isEnabled() {
        // Le compte est actif seulement si le statut est 'actif'
        return "actif".equals(this.statut);
    }
}