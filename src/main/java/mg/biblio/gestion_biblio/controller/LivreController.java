package mg.biblio.gestion_biblio.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping; // Correction 1: Importer la classe Model

import mg.biblio.gestion_biblio.model.Livre;
import mg.biblio.gestion_biblio.repository.LivreRepository;

@Controller
public class LivreController {

    // Correction 2: Déclarer le repository et l'injecter via le constructeur
    private final LivreRepository livreRepository;

    public LivreController(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    @GetMapping("/livres")
    // Correction 3: Ajouter l'objet Model en paramètre de la méthode
    public String showLivresPage(Model model) {
        // Correction 4: Gérer les exceptions de manière plus propre
        try {
            // La logique pour récupérer les livres est maintenant au bon endroit
            List<Livre> livres = livreRepository.findAllWithExemplaires();

            // On ajoute la liste des livres au modèle pour la rendre accessible à la vue
            model.addAttribute("livres", livres);

        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des livres : " + e.getMessage());
            model.addAttribute("errorMessage", "Impossible de charger la liste des livres pour le moment.");
            model.addAttribute("livres", List.of());
        }

        // Retourne le nom de la vue HTML à afficher
        return "livres"; // Cherche le fichier templates/livres.html
    }
}