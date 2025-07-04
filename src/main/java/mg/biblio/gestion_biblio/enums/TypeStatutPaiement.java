package mg.biblio.gestion_biblio.enums;

public enum TypeStatutPaiement {
    EN_ATTENTE("en_attente"),
    PAYE("paye"),
    ANNULE("annule"),
    REMBOURSE("rembourse");

    private final String valeur;

    TypeStatutPaiement(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
}