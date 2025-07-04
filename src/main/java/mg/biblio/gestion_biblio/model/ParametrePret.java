package mg.biblio.gestion_biblio.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parametre_pret")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametrePret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parametre")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_adherent", nullable = false, unique = true)
    private TypeAdherent typeAdherent;

    @Column(name = "max_livres", nullable = false)
    private Integer maxLivres = 3;

    @Column(name = "duree_max_pret_jours", nullable = false)
    private Integer dureeMaxPretJours = 14;

    @Column(name = "penalite_par_jour", precision = 8, scale = 2)
    private BigDecimal penaliteParJour = BigDecimal.valueOf(0.50);

    @Column(name = "jours_tolerance")
    private Integer joursTolerance = 2;

    @Column(name = "peut_prolonger")
    private Boolean peutProlonger = true;

    @Column(name = "nb_max_prolongements")
    private Integer nbMaxProlongements = 2;

    @Column(name = "actif")
    private Boolean actif = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
