package com.biblio.projetBiblio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.biblio.projetBiblio.repository.*;

@Controller
public class LivreController {
    private final LivreRepository livreRepository;

    public LivreController(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    @GetMapping("/livres")
    public String afficherLivres(Model model) {
        model.addAttribute("livres", livreRepository.findAll());
        return "livres";
    }
}
