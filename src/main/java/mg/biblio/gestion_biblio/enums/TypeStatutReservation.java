package mg.biblio.gestion_biblio.enums;

public enum TypeStatutReservation {
    EN_ATTENTE("en_attente"),
    SATISFAITE("satisfaite"),
    EXPIREE("expiree"),
    ANNULEE("annulee");

    private final String valeur;

    TypeStatutReservation(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
}