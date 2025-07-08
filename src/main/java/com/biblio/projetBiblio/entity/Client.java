package com.biblio.projetBiblio.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private Integer age;

    @ManyToOne
    @JoinColumn(name = "id_typeclient")
    private TypeClient typeClient;

    @Column(name = "date_enregistrement")
    private LocalDate dateEnregistrement;

    @Column(name = "fin_abonnement")
    private LocalDate finAbonnement;

    @OneToMany(mappedBy = "client")
    private List<Pret> prets;

    @OneToMany(mappedBy = "client")
    private List<Penalite> penalites;

    private Boolean actif;

    // Constructeurs
    public Client() {
        this.dateEnregistrement = LocalDate.now();
    }

    // Getters & Setters

    public List<Pret> getPrets() {
        return prets;
    }

    public void setPrets(List<Pret> prets) {
        this.prets = prets;
    }

    public List<Penalite> getPenalites() {
        return penalites;
    }

    public void setPenalites(List<Penalite> penalites) {
        this.penalites = penalites;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public TypeClient getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(TypeClient typeClient) {
        this.typeClient = typeClient;
    }

    public LocalDate getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(LocalDate dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public boolean isActif() {
        return actif;
    }

    public boolean getActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public LocalDate getFinAbonnement() {
        return finAbonnement;
    }

    public void setFinAbonnement(LocalDate finAbonnement) {
        this.finAbonnement = finAbonnement;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }
    
}
