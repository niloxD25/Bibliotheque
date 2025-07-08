package com.biblio.projetBiblio.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "typeclients")
public class TypeClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Column(name = "nbr_livre_max")
    private Integer nbrLivreMax;

    @Column(name = "jours_penalisation")
    private Integer joursPenalisation;

    @Column(name = "nbr_jours_emprunt")
    private Integer nbrJoursEmprunt;

    @Column(name = "nbr_prolongement")
    private Integer nbrProlongement;

    @Column(name = "nbr_reservation")
    private Integer nbrReservation;

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNbrLivreMax() {
        return nbrLivreMax;
    }

    public void setNbrLivreMax(Integer nbrLivreMax) {
        this.nbrLivreMax = nbrLivreMax;
    }

    public Integer getJoursPenalisation() {
        return joursPenalisation;
    }

    public void setJoursPenalisation(Integer joursPenalisation) {
        this.joursPenalisation = joursPenalisation;
    }

    public Integer getNbrJoursEmprunt() {
        return nbrJoursEmprunt;
    }

    public void setNbrJoursEmprunt(Integer nbrJoursEmprunt) {
        this.nbrJoursEmprunt = nbrJoursEmprunt;
    }

    public Integer getNbrProlongement() {
        return nbrProlongement;
    }

    public void setNbrProlongement(Integer nbrProlongement) {
        this.nbrProlongement = nbrProlongement;
    }

    public Integer getNbrReservation() {
        return nbrReservation;
    }

    public void setNbrReservation(Integer nbrReservation) {
        this.nbrReservation = nbrReservation;
    }

    
}
