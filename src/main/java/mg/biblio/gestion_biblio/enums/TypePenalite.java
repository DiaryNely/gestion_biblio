package mg.biblio.gestion_biblio.enums;

public enum TypePenalite {
    RETARD("retard"),
    DETERIORATION("deterioration"),
    PERTE("perte"),
    AUTRE("autre");

    private final String valeur;

    TypePenalite(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
}
