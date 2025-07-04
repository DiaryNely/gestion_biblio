package mg.biblio.gestion_biblio.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "livre")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livre")
    private Long id;

    @Column(name = "titre", nullable = false)
    private String titre;

    @Column(name = "date_edition")
    private LocalDate dateEdition;

    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_langue")
    private Langue langue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_genre")
    private GenreLitteraire genre;

    @Column(name = "mots_cles", columnDefinition = "TEXT")
    private String motsCles;

    @Column(name = "rayon", length = 50)
    private String rayon;

    @Column(name = "etagere", length = 50)
    private String etagere;

    @Column(name = "resume", columnDefinition = "TEXT")
    private String resume;

    @Column(name = "editeur")
    private String editeur;

    @Column(name = "nombre_pages")
    private Integer nombrePages;

    @Column(name = "actif")
    private Boolean actif = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "livre_auteur", joinColumns = @JoinColumn(name = "id_livre"), inverseJoinColumns = @JoinColumn(name = "id_auteur"))
    private Set<Auteur> auteurs;

    @OneToMany(mappedBy = "livre", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Exemplaire> exemplaires;

    @OneToMany(mappedBy = "livre", fetch = FetchType.LAZY)
    private List<Reservation> reservations;

}
