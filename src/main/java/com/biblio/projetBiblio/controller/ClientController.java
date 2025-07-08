package com.biblio.projetBiblio.controller;

import com.biblio.projetBiblio.dto.ClientDTO;
import com.biblio.projetBiblio.entity.Client;
import com.biblio.projetBiblio.entity.Penalite;
import com.biblio.projetBiblio.repository.ClientRepository;
import com.biblio.projetBiblio.repository.PenaliteRepository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class ClientController {

    private final ClientRepository clientRepository;
    private final PenaliteRepository penaliteRepository;

    public ClientController(ClientRepository clientRepository, PenaliteRepository penaliteRepository) {
        this.clientRepository = clientRepository;
        this.penaliteRepository = penaliteRepository;
    }

    @GetMapping("/clients")
    public String listeClients(Model model) {
        List<Client> clients = clientRepository.findAll(Sort.by("id"));

        List<ClientDTO> clientDTOs = clients.stream().map(client -> {
            ClientDTO dto = new ClientDTO();
            dto.setId(client.getId());
            dto.setNom(client.getNom());
            dto.setAge(client.getAge());
            dto.setDateEnregistrement(client.getDateEnregistrement());
            dto.setActif(client.getActif());
            dto.setFinAbonnement(client.getFinAbonnement());

            LocalDate today = LocalDate.now();
            Optional<Penalite> penaliteOpt = penaliteRepository
                    .findTopByClientIdAndDateFinGreaterThanEqualOrderByDateFinDesc(client.getId(), today);

            if (penaliteOpt.isPresent()) {
                Penalite penalite = penaliteOpt.get();
                dto.setPenalise(true);
                dto.setDateDebutPenalite(penalite.getDateDebut());
                dto.setDateFinPenalite(penalite.getDateFin());
            } else {
                dto.setPenalise(false);
            }

            return dto;
        }).toList();

        model.addAttribute("clients", clientDTOs);
        return "clients";
    }

    @PostMapping("/clients/{id}/toggle-actif")
    @Transactional
    public String toggleActif(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé : " + id));

        client.setActif(!client.getActif());
        clientRepository.save(client);

        if (client.getActif()) {
            redirectAttributes.addFlashAttribute("message", "Le client a été abonné.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Le client a été révoqué.");
        }

        return "redirect:/clients";
    }


}
