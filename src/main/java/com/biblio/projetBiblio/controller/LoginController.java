package com.biblio.projetBiblio.controller;

import com.biblio.projetBiblio.entity.Client;
import com.biblio.projetBiblio.repository.ClientRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final ClientRepository clientRepository;

    public LoginController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String nom, HttpSession session, Model model) {
        Client client = clientRepository.findByNomIgnoreCase(nom).orElse(null);

        if (client == null) {
            model.addAttribute("erreur", "Client introuvable !");
            return "login";
        }

        session.setAttribute("clientConnecte", client);
        return "redirect:/accueil";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
