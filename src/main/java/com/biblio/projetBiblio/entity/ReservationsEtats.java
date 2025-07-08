package com.biblio.projetBiblio.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations_etats")
public class ReservationsEtats {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "id_reservation")
    private Reservation reservation;

    @ManyToOne @JoinColumn(name = "id_statut")
    private ReservationStatut statut;

    private LocalDateTime dateStatut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public ReservationStatut getStatut() {
        return statut;
    }

    public void setStatut(ReservationStatut statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateStatut() {
        return dateStatut;
    }

    public void setDateStatut(LocalDateTime dateStatut) {
        this.dateStatut = dateStatut;
    }

    
}

