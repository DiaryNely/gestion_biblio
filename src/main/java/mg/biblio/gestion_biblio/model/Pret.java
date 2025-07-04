package mg.biblio.gestion_biblio.model;

import java.time.LocalDate;
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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.biblio.gestion_biblio.enums.TypeEtatPhysique;

@Entity
@Table(name = "pret")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pret")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_exemplaire", nullable = false)
    private Exemplaire exemplaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adherent", nullable = false)
    private Adherent adherent;

    @Column(name = "date_emprunt", nullable = false)
    private LocalDate dateEmprunt = LocalDate.now();

    @Column(name = "date_retour_prevue", nullable = false)
    private LocalDate dateRetourPrevue;

    @Column(name = "date_retour_reelle")
    private LocalDate dateRetourReelle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type", nullable = false)
    private TypePret typePret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etat", nullable = false)
    private EtatPret etatPret;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_livre_emprunt")
    private TypeEtatPhysique etatLivreEmprunt = TypeEtatPhysique.BON;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_livre_retour")
    private TypeEtatPhysique etatLivreRetour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bibliothecaire_emprunt_id")
    private Utilisateur bibliothecaireEmprunt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bibliothecaire_retour_id")
    private Utilisateur bibliothecaireRetour;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "pret", fetch = FetchType.LAZY)
    private List<Penalite> penalites;

    @OneToMany(mappedBy = "pret", fetch = FetchType.LAZY)
    private List<ProlongementPret> prolongements;
}