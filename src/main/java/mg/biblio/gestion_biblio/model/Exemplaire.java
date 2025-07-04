package mg.biblio.gestion_biblio.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.biblio.gestion_biblio.enums.TypeEtatPhysique;

@Entity
@Table(name = "exemplaire")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exemplaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exemplaire")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livre", nullable = false)
    private Livre livre;

    @Column(name = "code_inventaire", unique = true, nullable = false, length = 50)
    private String codeInventaire;

    @Column(name = "etat_physique")
    private TypeEtatPhysique etatPhysique = TypeEtatPhysique.BON;

    @Column(name = "disponible")
    private Boolean disponible = true;

    @Column(name = "date_acquisition")
    private LocalDate dateAcquisition = LocalDate.now();

    @Column(name = "prix_achat", precision = 10, scale = 2)
    private BigDecimal prixAchat;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "exemplaire", fetch = FetchType.LAZY)
    private List<Pret> prets;
}