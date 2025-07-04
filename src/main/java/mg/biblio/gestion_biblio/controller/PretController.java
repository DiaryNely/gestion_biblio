package mg.biblio.gestion_biblio.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mg.biblio.gestion_biblio.exception.PretException;
import mg.biblio.gestion_biblio.service.PretService;

@Controller
@RequestMapping("/api/prets") // On utilise /api pour les actions de données
public class PretController {

    private final PretService pretService;

    public PretController(PretService pretService) {
        this.pretService = pretService;
    }

    @PostMapping("/emprunter/{idExemplaire}")
    @ResponseBody // Important: renvoie des données (JSON), pas une vue HTML
    public ResponseEntity<?> emprunter(@PathVariable Long idExemplaire) {
        try {
            pretService.emprunterLivre(idExemplaire);
            // Si tout s'est bien passé, on renvoie un message de succès
            return ResponseEntity
                    .ok(Map.of("message", "Emprunt réussi ! Vous avez jusqu'au [date] pour le retourner."));
        } catch (PretException e) {
            // Si une de nos règles de gestion a levé une exception
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Pour toute autre erreur imprévue
            return ResponseEntity.status(500).body(Map.of("error", "Une erreur interne est survenue."));
        }
    }
}
