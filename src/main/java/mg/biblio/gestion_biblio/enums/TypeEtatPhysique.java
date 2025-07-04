package mg.biblio.gestion_biblio.enums;

public enum TypeEtatPhysique {
    NEUF("Neuf"),
    BON("Bon"),
    MOYEN("Moyen"),
    ABIME("ABIME"),
    INUTILISABLE("Inutilisable");

    private final String valeur;

    TypeEtatPhysique(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
}