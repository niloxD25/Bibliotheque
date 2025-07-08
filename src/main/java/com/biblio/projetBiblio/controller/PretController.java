package com.biblio.projetBiblio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.biblio.projetBiblio.repository.*;

import jakarta.servlet.http.HttpSession;

import com.biblio.projetBiblio.entity.*;

@Controller
public class PretController {

    private final LivreRepository livreRepository;
    private final ClientRepository clientRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final PretRepository pretRepository;
    private final PenaliteRepository penaliteRepository;
    private final PretsEtatsRepository pretsEtatsRepository;
    private final EtatsRepository etatsRepository;
    private final JourFerieRepository jourFerieRepository;

    public PretController(
        LivreRepository livreRepository,
        ClientRepository clientRepository,
        ExemplaireRepository exemplaireRepository,
        PretRepository pretRepository,
        PenaliteRepository penaliteRepository,
        PretsEtatsRepository pretsEtatsRepository,
        EtatsRepository etatsRepository,
        JourFerieRepository jourFerieRepository
    ) {
        this.livreRepository = livreRepository;
        this.clientRepository = clientRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.pretRepository = pretRepository;
        this.penaliteRepository = penaliteRepository;
        this.pretsEtatsRepository = pretsEtatsRepository;
        this.etatsRepository = etatsRepository;
        this.jourFerieRepository = jourFerieRepository;
    }

    @GetMapping("/livres/{id}/pret")
    public String formulairePret(@PathVariable Long id, Model model) {
        var livre = livreRepository.findById(id).orElseThrow();
        var clients = clientRepository.findAll(Sort.by("id"));
        var exemplairesDisponibles = exemplaireRepository.findByLivreIdAndDisponibleTrue(id);

        model.addAttribute("livre", livre);
        model.addAttribute("clients", clients);
        model.addAttribute("exemplaires", exemplairesDisponibles);
        model.addAttribute("pret", new PretForm());

        return "formulairePret";
    }

    @PostMapping("/livres/{id}/pret")
    @Transactional
    public String soumettrePret(
            @PathVariable Long id,
            @ModelAttribute PretForm pretForm,
            Model model
    ) {
        var client = clientRepository.findById(pretForm.getClientId()).orElseThrow();
        var exemplaire = exemplaireRepository.findById(pretForm.getExemplaireId()).orElseThrow();
        var livre = livreRepository.findById(id).orElseThrow();

        long nombrePretsActifs = pretRepository.countByClientIdAndDateRetourEffectiveIsNull(client.getId());

        if (!client.isActif()) {
            model.addAttribute("erreur", "Le client n'a pas d'abonnement actif." + client.isActif());
            return formulairePretErreur(id, model);
        }

        if (nombrePretsActifs >= client.getTypeClient().getNbrLivreMax()) {
            model.addAttribute("erreur", "Le client a atteint le nombre maximum de prêts autorisés.");
            return formulairePretErreur(id, model);
        }

        if (penaliteRepository.isClientPenalised(client.getId(), LocalDate.now())) {
            model.addAttribute("erreur", "Le client est actuellement en période de pénalité.");
            return formulairePretErreur(id, model);
        }

        if (client.getAge() < livre.getAge()) {
            model.addAttribute("erreur", "Le client n’a pas l’âge requis pour emprunter ce livre.");
            return formulairePretErreur(id, model);
        }

        // 🔷 Enregistrer le prêt
        Pret pret = new Pret();
        pret.setClient(client);
        pret.setExemplaire(exemplaire);
        pret.setDateEmprunt(pretForm.getDateEmprunt());

        // 🔷 Calcule de la date retour prévue
        LocalDate retourPrevue = pretForm.getDateEmprunt()
                .plusDays(client.getTypeClient().getNbrJoursEmprunt() - 1);

        List<LocalDate> joursFeries = jourFerieRepository.findAllDates();

        while (joursFeries.contains(retourPrevue)) {
            retourPrevue = retourPrevue.plusDays(1);
        }

        pret.setDateRetourPrevue(retourPrevue);
        pretRepository.save(pret);

        // 🔷 Marquer exemplaire indisponible
        exemplaire.setDisponible(false);
        exemplaireRepository.save(exemplaire);

        // 🔷 Enregistrer l’état initial du prêt
        Etat etatInitial = etatsRepository.findByNomIgnoreCase("En cours")
                .orElseThrow(() -> new IllegalStateException("État 'En cours' non trouvé"));

        PretsEtats pretsEtats = new PretsEtats();
        pretsEtats.setPret(pret);
        pretsEtats.setEtat(etatInitial);
        pretsEtats.setDateEtat(LocalDate.now().atStartOfDay());
        pretsEtatsRepository.save(pretsEtats);

        return "redirect:/livres";
    }

    private String formulairePretErreur(Long livreId, Model model) {
        var livre = livreRepository.findById(livreId).orElseThrow();
        var clients = clientRepository.findAll();
        var exemplairesDisponibles = exemplaireRepository.findByLivreIdAndDisponibleTrue(livreId);

        model.addAttribute("livre", livre);
        model.addAttribute("clients", clients);
        model.addAttribute("exemplaires", exemplairesDisponibles);
        model.addAttribute("pret", new PretForm());
        return "formulairePret";
    }

    public static class PretForm {
        private Long clientId;
        private Long exemplaireId;
        private LocalDate dateEmprunt;

        // getters et setters
        public Long getClientId() { return clientId; }
        public void setClientId(Long clientId) { this.clientId = clientId; }
        public Long getExemplaireId() { return exemplaireId; }
        public void setExemplaireId(Long exemplaireId) { this.exemplaireId = exemplaireId; }
        public LocalDate getDateEmprunt() { return dateEmprunt; }
        public void setDateEmprunt(LocalDate dateEmprunt) { this.dateEmprunt = dateEmprunt; }
    }

    @GetMapping("/prets")
    public String listePrets(Model model) {
        List<Pret> prets = pretRepository.findAll(Sort.by("id"));

        List<PretDTOO> pretsDTO = prets.stream()
                .map(pret -> {
                    List<PretsEtats> etats = pretsEtatsRepository.findByPretIdOrderByDateEtatDesc(pret.getId());
                    PretsEtats dernierEtat = etats.isEmpty() ? null : etats.get(0);
                    return new PretDTOO(pret, dernierEtat != null ? dernierEtat.getEtat() : null);
                })
                .toList();

        model.addAttribute("prets", pretsDTO);
        return "prets";
    }

    @PostMapping("/prets/{id}/retourner")
    @Transactional
    public String retournerPret(
            @PathVariable Long id,
            @RequestParam("dateRetourEffective") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateRetourEffective,
            RedirectAttributes redirectAttributes) {

        Pret pret = pretRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Prêt non trouvé avec id: " + id));

        if (pret.getDateRetourEffective() != null) {
            redirectAttributes.addFlashAttribute("message", "Ce prêt est déjà terminé.");
            return "redirect:/prets";
        }

        pret.setDateRetourEffective(dateRetourEffective);
        pretRepository.save(pret);

        Exemplaire exemplaire = pret.getExemplaire();
        exemplaire.setDisponible(true);
        exemplaireRepository.save(exemplaire);

        Etat etatRetourne = etatsRepository.findByNomIgnoreCase("Rendu")
                .orElseThrow(() -> new IllegalArgumentException("État 'Rendu' non trouvé"));

        PretsEtats pretsEtats = new PretsEtats();
        pretsEtats.setPret(pret);
        pretsEtats.setEtat(etatRetourne);
        pretsEtats.setDateEtat(dateRetourEffective.atStartOfDay());
        pretsEtatsRepository.save(pretsEtats);

        redirectAttributes.addFlashAttribute("message", "Prêt retourné le " + dateRetourEffective + ".");
        return "redirect:/prets";
    }


    @PostMapping("/prets/{id}/penaliser")
    @Transactional
    public String penaliserPret(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Charger le prêt
        Pret pret = pretRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prêt non trouvé avec id: " + id));

        Client client = pret.getClient();

        int joursPenalisation = client.getTypeClient().getJoursPenalisation();
        LocalDate aujourdHui = LocalDate.now();

        // Chercher la pénalité en cours (la plus récente dont la dateFin >= aujourd’hui)
        Penalite penaliteExistante = penaliteRepository
                .findTopByClientIdAndDateFinGreaterThanEqualOrderByDateFinDesc(client.getId(), aujourdHui)
                .orElse(null);

        if (penaliteExistante != null) {
            // il a déjà une pénalité en cours : on prolonge la dateFin
            penaliteExistante.setDateFin(penaliteExistante.getDateFin().plusDays(joursPenalisation));
            penaliteRepository.save(penaliteExistante);
        } else {
            // sinon, créer une nouvelle pénalité
            Penalite penalite = new Penalite();
            penalite.setClient(client);
            penalite.setDateDebut(aujourdHui);
            penalite.setDateFin(aujourdHui.plusDays(joursPenalisation));
            penalite.setRaison("Pénalité pour prêt n°" + id);
            penaliteRepository.save(penalite);
        }

        // 🔷 Mettre à jour l’état du prêt en « Retard »
        Etat etatRetard = etatsRepository.findByNomIgnoreCase("Retard")
                .orElseThrow(() -> new IllegalArgumentException("État 'Retard' non trouvé"));

        PretsEtats pretsEtats = new PretsEtats();
        pretsEtats.setPret(pret);
        pretsEtats.setEtat(etatRetard);
        pretsEtats.setDateEtat(LocalDateTime.now());
        pretsEtatsRepository.save(pretsEtats);

        redirectAttributes.addFlashAttribute("message", "Pénalité ajoutée ou prolongée.");
        return "redirect:/prets";
    }


    @PostMapping("/prets/{id}/prolonger")
    @Transactional
    public String prolongerPret(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Charger le prêt
        Pret pret = pretRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prêt non trouvé avec id: " + id));

        if (pret.getDateRetourEffective() != null) {
            redirectAttributes.addFlashAttribute("erreur", "Ce prêt est déjà terminé et ne peut pas être prolongé.");
            return "redirect:/prets";
        }

        // Récupérer le client
        Client client = pret.getClient();

        // Vérifier si le client est en période de pénalité
        LocalDate aujourdHui = LocalDate.now();
        boolean penalise = penaliteRepository.isClientPenalised(client.getId(), aujourdHui);

        if (penalise) {
            redirectAttributes.addFlashAttribute("erreur", "Le client est actuellement en période de pénalité. Prolongation impossible.");
            return "redirect:/prets";
        }

        // Vérifier la date de fin d’abonnement
        LocalDate finAbonnement = client.getFinAbonnement();
        if (finAbonnement == null) {
            redirectAttributes.addFlashAttribute("erreur", "Impossible de prolonger : la date de fin d’abonnement est inconnue.");
            return "redirect:/prets";
        }

        // Calculer la nouvelle date
        int joursProlongation = client.getTypeClient().getNbrJoursEmprunt();
        LocalDate nouvelleDate = pret.getDateRetourPrevue().plusDays(joursProlongation);

        if (nouvelleDate.isAfter(finAbonnement)) {
            redirectAttributes.addFlashAttribute("erreur",
                    "La nouvelle date de retour (" + nouvelleDate + ") dépasse la fin d’abonnement (" + finAbonnement + ").");
            return "redirect:/prets";
        }

        // Prolonger
        pret.setDateRetourPrevue(nouvelleDate);
        pretRepository.save(pret);

        // État "Prolongé"
        Etat etatProlonge = etatsRepository.findByNomIgnoreCase("Prolongé")
                .orElseThrow(() -> new IllegalArgumentException("État 'Prolongé' non trouvé"));

        PretsEtats pretsEtats = new PretsEtats();
        pretsEtats.setPret(pret);
        pretsEtats.setEtat(etatProlonge);
        pretsEtats.setDateEtat(LocalDateTime.now());
        pretsEtatsRepository.save(pretsEtats);

        redirectAttributes.addFlashAttribute("message", "Prêt prolongé jusqu’au " + nouvelleDate + ".");
        return "redirect:/prets";
    }



    @GetMapping("/mes-prets")
    public String mesPrets(Model model, HttpSession session) {
        Client client = (Client) session.getAttribute("clientConnecte");
        if (client == null) {
            return "redirect:/login";
        }

        List<Pret> prets = pretRepository.findByClientId(client.getId());

        List<PretDTOO> pretsDTO = prets.stream()
                .map(pret -> {
                    List<PretsEtats> etats = pretsEtatsRepository.findByPretIdOrderByDateEtatDesc(pret.getId());
                    PretsEtats dernierEtat = etats.isEmpty() ? null : etats.get(0);
                    return new PretDTOO(pret, dernierEtat != null ? dernierEtat.getEtat() : null);
                })
                .toList();

        model.addAttribute("prets", pretsDTO);
        model.addAttribute("client", client);
        return "mes-prets";
    }
}

