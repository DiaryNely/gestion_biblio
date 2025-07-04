package mg.biblio.gestion_biblio.enums;

public enum TypeStatutUtilisateur {
    ACTIF("actif"),
    SUSPENDU("suspendu"),
    INACTIF("inactif");

    private final String valeur;

    TypeStatutUtilisateur(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
}