package mg.biblio.gestion_biblio.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.biblio.gestion_biblio.enums.TypePenalite;
import mg.biblio.gestion_biblio.enums.TypeStatutPaiement;

@Entity
@Table(name = "penalite")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Penalite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_penalite")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adherent", nullable = false)
    private Adherent adherent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pret")
    private Pret pret;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_penalite", nullable = false)
    private TypePenalite typePenalite;

    @Column(name = "montant", nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private TypeStatutPaiement statut = TypeStatutPaiement.EN_ATTENTE;

    @Column(name = "motif", columnDefinition = "TEXT")
    private String motif;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}