package mg.biblio.gestion_biblio.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auteur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auteur")
    private Long id;

    @Column(name = "nom_complet", nullable = false, unique = true)
    private String nomComplet;

    @Column(name = "biographie", columnDefinition = "TEXT")
    private String biographie;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "date_deces")
    private LocalDate dateDeces;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "auteurs", fetch = FetchType.LAZY)
    private Set<Livre> livres;
}