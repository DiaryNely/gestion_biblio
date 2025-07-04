package mg.biblio.gestion_biblio.enums;

public enum TypePaiementSource {
    ABONNEMENT("abonnement"),
    PENALITE("penalite"),
    DON("don"),
    AUTRE("autre");

    private final String valeur;

    TypePaiementSource(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
}