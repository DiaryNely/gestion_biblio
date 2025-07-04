package mg.biblio.gestion_biblio.dto;

import lombok.Data;
import java.util.List;

@Data
public class DashboardStatsDTO {
    // Stats Livres & Exemplaires
    private long nombreTotalLivres;
    private long nombreTotalExemplaires;
    private long nombreExemplairesDisponibles;
    private long nombreExemplairesEmpruntes;
    private long nombreExemplairesAbimes;

    // Stats Adhérents & Utilisateurs
    private long nombreTotalUtilisateurs;
    private long nombreTotalAdherents;

    // Stats Prêts
    private long nombrePretsEnCours;
    private long nombrePretsEnRetard;

    // Listes pour les classements
    private List<LivreEmpruntStatsDTO> livresPlusEmpruntes;
}