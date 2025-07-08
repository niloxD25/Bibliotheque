package com.biblio.projetBiblio.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biblio.projetBiblio.repository.*;

import jakarta.servlet.http.HttpSession;

import com.biblio.projetBiblio.entity.*;

@Controller
@RequestMapping("/prolongements")
public class ProlongementController {

    private final PretRepository pretRepository;
    private final ProlongementRepository prolongementRepository;

    public ProlongementController(PretRepository pretRepository, ProlongementRepository prolongementRepository) {
        this.pretRepository = pretRepository;
        this.prolongementRepository = prolongementRepository;
    }

    @PostMapping("/demande")
    @Transactional
    public String demanderProlongement(@RequestParam Long pretId, RedirectAttributes redirectAttributes) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new IllegalArgumentException("Prêt introuvable"));

        Client client = pret.getClient();

        // Vérifie si une demande active existe déjà
        boolean existeDeja = prolongementRepository.findByPretIdAndActifTrue(pretId)
                .stream()
                .findAny()
                .isPresent();

        if (existeDeja) {
            redirectAttributes.addFlashAttribute("erreur", "Une demande de prolongement est déjà en attente.");
            return "redirect:/mes-prets";
        }

        // Récupère le nombre maximal de prolongements actifs autorisés
        int maxProlongements = client.getTypeClient().getNbrProlongement();

        // Compte les demandes actives pour ce client et ce prêt
        long nombreActifs = prolongementRepository.countActifsForClient(client.getId());

        if (nombreActifs >= maxProlongements) {
            redirectAttributes.addFlashAttribute("erreur", "Vous avez atteint le nombre maximal de prolongements autorisés.");
            return "redirect:/mes-prets";
        }

        // Sinon, crée la demande
        Prolongement prolongement = new Prolongement();
        prolongement.setPret(pret);
        prolongement.setDateDemande(LocalDateTime.now());
        prolongement.setActif(true);
        prolongementRepository.save(prolongement);

        redirectAttributes.addFlashAttribute("message", "Votre demande de prolongement a été enregistrée." + nombreActifs);
        return "redirect:/mes-prets";
    }


    @GetMapping("/mes")
    public String mesProlongements(HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("clientConnecte");
        if (client == null) {
            return "redirect:/login";
        }

        List<Prolongement> demandes = prolongementRepository.findByPretClientIdOrderByDateDemandeDesc(client.getId());
        model.addAttribute("demandes", demandes);
        return "mes_prolongements";
    }

    @GetMapping("")
    public String listeProlongements(Model model) {
        List<Prolongement> prolongements = prolongementRepository.findAll();

        model.addAttribute("prolongements", prolongements);
        return "prolongements";
    }

}
