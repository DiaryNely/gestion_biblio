package mg.biblio.gestion_biblio.enums;

public enum TypeAbonnement {
    MENSUEL("mensuel"),
    ANNUEL("annuel"),
    PERMANENT("permanent");

    private final String valeur;

    TypeAbonnement(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
}