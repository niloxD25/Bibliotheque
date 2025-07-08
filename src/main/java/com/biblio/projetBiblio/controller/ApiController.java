package com.biblio.projetBiblio.controller;

import com.biblio.projetBiblio.dto.*;
import com.biblio.projetBiblio.entity.Livre;
import com.biblio.projetBiblio.entity.Client;
import com.biblio.projetBiblio.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final LivreRepository livreRepository;
    private final ClientRepository clientRepository;

    public ApiController(LivreRepository livreRepository, ClientRepository clientRepository) {
        this.livreRepository = livreRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/livres")
    public List<LivreDTO> getLivres() {
        return livreRepository.findAll().stream().map(this::toLivreDTO).toList();
    }

    @GetMapping("/livres/{id}")
    public LivreDTO getLivreById(@PathVariable Long id) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livre non trouvé avec id: " + id));
        return toLivreDTO(livre);
    }

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(this::toClientDTO).toList();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec id: " + id));
        return toClientDTO(client);
    }

    // 🔷 Méthodes utilitaires pour mapper les entités vers DTOs
    private LivreDTO toLivreDTO(Livre livre) {
        LivreDTO dto = new LivreDTO();
        dto.setId(livre.getId());
        dto.setTitre(livre.getTitre());
        dto.setAuteur(livre.getAuteur());
        dto.setEditeur(livre.getEditeur());
        dto.setAge(livre.getAge());
        dto.setGenre(livre.getGenre() != null ? livre.getGenre().getNom() : null);
        dto.setExemplaires(
                livre.getExemplaires().stream()
                        .map(ex -> {
                            ExemplaireDTO exDto = new ExemplaireDTO();
                            exDto.setId(ex.getId());
                            exDto.setDisponible(ex.getDisponible());
                            return exDto;
                        }).toList()
        );
        return dto;
    }

    private ClientDTO toClientDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setAge(client.getAge());
        dto.setTypeClient(client.getTypeClient().getType());

        dto.setPrets(
                client.getPrets().stream()
                        .map(pret -> {
                            PretDTO pretDTO = new PretDTO();
                            pretDTO.setId(pret.getId());
                            pretDTO.setLivreTitre(pret.getExemplaire().getLivre().getTitre());
                            pretDTO.setDateEmprunt(pret.getDateEmprunt());
                            pretDTO.setDateRetourPrevue(pret.getDateRetourPrevue());
                            pretDTO.setDateRetourEffective(pret.getDateRetourEffective());
                            return pretDTO;
                        }).toList()
        );

        dto.setPenalites(
                client.getPenalites().stream()
                        .map(penalite -> {
                            PenaliteDTO pDto = new PenaliteDTO();
                            pDto.setId(penalite.getId());
                            pDto.setDateDebut(penalite.getDateDebut());
                            pDto.setDateFin(penalite.getDateFin());
                            pDto.setRaison(penalite.getRaison());
                            return pDto;
                        }).toList()
        );

        return dto;
    }
}
