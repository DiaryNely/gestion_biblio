package mg.biblio.gestion_biblio.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "prolongement_pret")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProlongementPret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prolongement")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pret", nullable = false)
    private Pret pret;

    @Column(name = "date_prolongement", nullable = false)
    private LocalDate dateProlongement = LocalDate.now();

    @Column(name = "ancienne_date_retour", nullable = false)
    private LocalDate ancienneDateRetour;

    @Column(name = "nouvelle_date_retour", nullable = false)
    private LocalDate nouvelleDateRetour;

    @Column(name = "motif", columnDefinition = "TEXT")
    private String motif;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_statut_prolongement")
    private StatutProlongement statutProlongement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approuve_par_id")
    private Utilisateur approuvePar;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}