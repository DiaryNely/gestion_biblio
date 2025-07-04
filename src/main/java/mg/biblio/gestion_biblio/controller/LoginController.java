package mg.biblio.gestion_biblio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        // Retourne le nom du fichier HTML (sans l'extension .html)
        // Spring va le chercher dans src/main/resources/templates/
        return "login";
    }
}