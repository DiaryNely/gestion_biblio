package mg.biblio.gestion_biblio.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import mg.biblio.gestion_biblio.dto.DashboardStatsDTO;
import mg.biblio.gestion_biblio.enums.TypeEtatPhysique;
import mg.biblio.gestion_biblio.repository.AdherentRepository;
import mg.biblio.gestion_biblio.repository.ExemplaireRepository;
import mg.biblio.gestion_biblio.repository.LivreRepository;
import mg.biblio.gestion_biblio.repository.PretRepository;
import mg.biblio.gestion_biblio.repository.UtilisateurRepository;

@Service
public class DashboardService {

    private final LivreRepository livreRepository;
    private final AdherentRepository adherentRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final PretRepository pretRepository;

    public DashboardService(LivreRepository livreRepository, AdherentRepository adherentRepository,
            UtilisateurRepository utilisateurRepository, ExemplaireRepository exemplaireRepository,
            PretRepository pretRepository) {
        this.livreRepository = livreRepository;
        this.adherentRepository = adherentRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.pretRepository = pretRepository;
    }

    public DashboardStatsDTO getComprehensiveDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        LocalDate aujourdhui = LocalDate.now();

        // --- Stats Livres & Exemplaires ---
        stats.setNombreTotalLivres(livreRepository.count());
        stats.setNombreTotalExemplaires(exemplaireRepository.count());
        stats.setNombreExemplairesDisponibles(exemplaireRepository.countByDisponible(true));
        stats.setNombreExemplairesEmpruntes(exemplaireRepository.countByDisponible(false));
        // On considère 'Abîmé' et 'Inutilisable' comme des états préoccupants
        long abimes = exemplaireRepository.countByEtatPhysique(TypeEtatPhysique.ABIME);
        long inutilisables = exemplaireRepository.countByEtatPhysique(TypeEtatPhysique.INUTILISABLE);
        stats.setNombreExemplairesAbimes(abimes + inutilisables);

        // --- Stats Adhérents & Utilisateurs ---
        stats.setNombreTotalUtilisateurs(utilisateurRepository.count());
        stats.setNombreTotalAdherents(adherentRepository.count());

        // --- Stats Prêts ---
        stats.setNombrePretsEnCours(pretRepository.countByDateRetourReelleIsNull());
        stats.setNombrePretsEnRetard(pretRepository.countByDateRetourReelleIsNullAndDateRetourPrevueBefore(aujourdhui));

        // --- Classements ---
        stats.setLivresPlusEmpruntes(pretRepository.findTop5MostLoanedBooks());

        return stats;
    }
}