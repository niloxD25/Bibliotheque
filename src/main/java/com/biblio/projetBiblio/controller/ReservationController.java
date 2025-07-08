package com.biblio.projetBiblio.controller;

import com.biblio.projetBiblio.entity.*;
import com.biblio.projetBiblio.repository.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final LivreRepository livreRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationStatutRepository statutRepository;
    private final ReservationsEtatsRepository etatsRepository;

    public ReservationController(LivreRepository livreRepository,
                                 ExemplaireRepository exemplaireRepository,
                                 ClientRepository clientRepository,
                                 ReservationRepository reservationRepository,
                                 ReservationStatutRepository statutRepository,
                                 ReservationsEtatsRepository etatsRepository) {
        this.livreRepository = livreRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.clientRepository = clientRepository;
        this.reservationRepository = reservationRepository;
        this.statutRepository = statutRepository;
        this.etatsRepository = etatsRepository;
    }

    @GetMapping("/livre/{id}")
    public String formReservation(@PathVariable Long id, Model model) {
        var livre = livreRepository.findById(id).orElseThrow();
        var exemplairesDispo = exemplaireRepository.findByLivreId(id);

        model.addAttribute("livre", livre);
        model.addAttribute("exemplaires", exemplairesDispo);
        return "formulaireReservation";
    }

    @PostMapping
    @Transactional
    public String reserver(HttpSession session,
                        @RequestParam Long livreId,
                        @RequestParam Long exemplaireId,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateSouhaitee,
                        RedirectAttributes redirectAttributes) {

        Client client = (Client) session.getAttribute("clientConnecte");
        
        int nbReservationsActives = reservationRepository.countByClientIdAndActifTrue(client.getId());
        int maxReservations = client.getTypeClient().getNbrReservation();

        if (nbReservationsActives >= maxReservations) {
            redirectAttributes.addFlashAttribute("erreur", "Vous avez atteint le nombre maximum de réservations autorisées (" + maxReservations + ").");
            return "redirect:/reservations/livre/" + livreId;
        }


        Exemplaire exemplaire = exemplaireRepository.findById(exemplaireId).orElseThrow();

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setExemplaire(exemplaire);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setDateSouhaitee(dateSouhaitee);
        reservation.setActif(true);
        reservationRepository.save(reservation);

        var statutAttente = statutRepository.findByNomIgnoreCase("EN_ATTENTE").orElseThrow();

        ReservationsEtats etat = new ReservationsEtats();
        etat.setReservation(reservation);
        etat.setStatut(statutAttente);
        etat.setDateStatut(LocalDateTime.now());
        etatsRepository.save(etat);

        redirectAttributes.addFlashAttribute("message", "Votre réservation a été enregistrée.");
        return "redirect:/accueil";
    }

    @GetMapping("/mes-reservations")
    public String mesReservations(HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("clientConnecte");
        if (client == null) {
            return "redirect:/login";
        }

        List<Reservation> reservations = reservationRepository.findByClientId(client.getId());
        model.addAttribute("reservations", reservations);
        model.addAttribute("client", client);
        return "mes-reservations";
    }

}
