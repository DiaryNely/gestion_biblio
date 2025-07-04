package mg.biblio.gestion_biblio.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.biblio.gestion_biblio.enums.TypeStatutAdhesion;

@Entity
@Table(name = "adherent")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adherent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adherent")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false, unique = true)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_adherent", nullable = false)
    private TypeAdherent typeAdherent;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_adhesion")
    private TypeStatutAdhesion statutAdhesion = TypeStatutAdhesion.ACTIF;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "adherent", fetch = FetchType.LAZY)
    private List<Paiement> paiements;

    @OneToMany(mappedBy = "adherent", fetch = FetchType.LAZY)
    private List<Abonnement> abonnements;

    @OneToMany(mappedBy = "adherent", fetch = FetchType.LAZY)
    private List<Pret> prets;

    @OneToMany(mappedBy = "adherent", fetch = FetchType.LAZY)
    private List<Penalite> penalites;

    @OneToMany(mappedBy = "adherent", fetch = FetchType.LAZY)
    private List<Reservation> reservations;
}
