package mg.biblio.gestion_biblio.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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
import mg.biblio.gestion_biblio.enums.TypePaiementSource;
import mg.biblio.gestion_biblio.enums.TypeStatutPaiement;

@Entity
@Table(name = "paiement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paiement")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adherent", nullable = false)
    private Adherent adherent;

    @Column(name = "montant", nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(name = "date_paiement")
    private LocalDateTime datePaiement = LocalDateTime.now();

    @Column(name = "mode_paiement", length = 50)
    private String modePaiement;

    @Column(name = "reference_paiement", columnDefinition = "TEXT")
    private String referencePaiement;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_paiement")
    private TypeStatutPaiement statutPaiement = TypeStatutPaiement.PAYE;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_paiement", nullable = false)
    private TypePaiementSource sourcePaiement;

    @Column(name = "id_objet_concerne")
    private Long idObjetConcerne;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
