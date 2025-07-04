package mg.biblio.gestion_biblio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping; // <-- Importez Model
import org.springframework.web.bind.annotation.RequestMapping;

import mg.biblio.gestion_biblio.dto.DashboardStatsDTO;
import mg.biblio.gestion_biblio.service.DashboardService;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        // Appeler la nouvelle méthode de service
        DashboardStatsDTO dashboardStats = dashboardService.getComprehensiveDashboardStats();

        // Envoyer l'objet DTO complet à la vue
        model.addAttribute("dashboardStats", dashboardStats);

        return "admin/dashboard";
    }
}