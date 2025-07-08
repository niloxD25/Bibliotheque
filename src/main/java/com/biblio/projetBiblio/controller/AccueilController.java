package com.biblio.projetBiblio.controller;

import com.biblio.projetBiblio.entity.Client;
import com.biblio.projetBiblio.repository.LivreRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccueilController {

    private final LivreRepository livreRepository;

    public AccueilController(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    @GetMapping("/accueil")
    public String accueil(HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("clientConnecte");

        if (client == null) {
            return "redirect:/";
        }

        model.addAttribute("client", client);
        model.addAttribute("livres", livreRepository.findAll());

        return "accueil";
    }
}
