package mg.biblio.gestion_biblio.enums;

public enum TypeStatutAdhesion {
    ACTIF("actif"),
    SUSPENDU("suspendu"),
    EXPIRE("expire");

    private final String valeur;

    TypeStatutAdhesion(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
}