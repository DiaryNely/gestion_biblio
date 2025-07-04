package mg.biblio.gestion_biblio.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "langue")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Langue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_langue")
    private Long id;

    @Column(name = "nom", nullable = false, unique = true, length = 50)
    private String nom;

    @Column(name = "code_iso", nullable = false, unique = true, length = 5)
    private String codeIso;

    @Column(name = "actif")
    private Boolean actif = true;

    @OneToMany(mappedBy = "langue", fetch = FetchType.LAZY)
    private List<Livre> livres;
}