package mg.biblio.gestion_biblio.converters;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import mg.biblio.gestion_biblio.enums.TypeEtatPhysique;

@Converter(autoApply = true) // autoApply = true évite d'avoir à annoter chaque champ
public class TypeEtatPhysiqueConverter implements AttributeConverter<TypeEtatPhysique, String> {

    /**
     * Convertit l'Enum Java en chaîne de caractères pour la BDD.
     * On utilise la valeur personnalisée (ex: "Bon").
     */
    @Override
    public String convertToDatabaseColumn(TypeEtatPhysique typeEtatPhysique) {
        if (typeEtatPhysique == null) {
            return null;
        }
        return typeEtatPhysique.getValeur();
    }

    /**
     * Convertit la chaîne de caractères de la BDD en Enum Java.
     * On cherche l'Enum qui correspond à la valeur (ex: "Bon").
     */
    @Override
    public TypeEtatPhysique convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(TypeEtatPhysique.values())
                .filter(c -> c.getValeur().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Valeur inconnue pour TypeEtatPhysique: " + dbData));
    }
}