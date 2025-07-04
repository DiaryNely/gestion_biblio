package mg.biblio.gestion_biblio.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parametre_systeme")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametreSysteme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parametre")
    private Long id;

    @Column(name = "cle", nullable = false, unique = true, length = 100)
    private String cle;

    @Column(name = "valeur", nullable = false, columnDefinition = "TEXT")
    private String valeur;

    @Column(name = "type_valeur", length = 20)
    private String typeValeur = "string";

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "categorie", length = 50)
    private String categorie;

    @Column(name = "modifiable")
    private Boolean modifiable = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Méthodes utilitaires pour la conversion de types
    public Integer getValeurAsInteger() {
        if ("integer".equals(typeValeur)) {
            return Integer.valueOf(valeur);
        }
        throw new IllegalStateException("Le paramètre n'est pas de type integer");
    }

    public BigDecimal getValeurAsBigDecimal() {
        if ("decimal".equals(typeValeur)) {
            return new BigDecimal(valeur);
        }
        throw new IllegalStateException("Le paramètre n'est pas de type decimal");
    }

    public Boolean getValeurAsBoolean() {
        if ("boolean".equals(typeValeur)) {
            return Boolean.valueOf(valeur);
        }
        throw new IllegalStateException("Le paramètre n'est pas de type boolean");
    }
}