package com.biblio.projetBiblio.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prets_etats")
public class PretsEtats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_pret")
    private Pret pret;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_etat")
    private Etat etat;

    @Column(name = "date_etat")
    private LocalDateTime dateEtat;

    // getters & setters…
    public Long getId() {
        return id;
    }

    public Pret getPret() {
        return pret;
    }

    public Etat getEtat() {
        return etat;
    }

    public LocalDateTime getDateEtat() {
        return dateEtat;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPret(Pret pret) {
        this.pret = pret;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public void setDateEtat(LocalDateTime dateEtat) {
        this.dateEtat = dateEtat;
    }
}
