package com.biblio.projetBiblio.dto;

import java.time.LocalDate;
import java.util.List;

public class ClientDTO {
    private Long id;
    private String nom;
    private Integer age;
    private String typeClient;
    private List<PretDTO> prets;
    private List<PenaliteDTO> penalites;

    private LocalDate dateEnregistrement;
    private LocalDate finAbonnement;
    private boolean penalise;
    private LocalDate dateDebutPenalite;
    private LocalDate dateFinPenalite;
    private boolean actif;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getTypeClient() { return typeClient; }
    public void setTypeClient(String typeClient) { this.typeClient = typeClient; }

    public List<PretDTO> getPrets() { return prets; }
    public void setPrets(List<PretDTO> prets) { this.prets = prets; }

    public List<PenaliteDTO> getPenalites() { return penalites; }
    
    public void setPenalites(List<PenaliteDTO> penalites) { this.penalites = penalites; }
    public LocalDate getDateEnregistrement() {
        return dateEnregistrement;
    }
    public void setDateEnregistrement(LocalDate dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }
    public boolean isPenalise() {
        return penalise;
    }
    public void setPenalise(boolean penalise) {
        this.penalise = penalise;
    }
    public LocalDate getDateDebutPenalite() {
        return dateDebutPenalite;
    }
    public void setDateDebutPenalite(LocalDate dateDebutPenalite) {
        this.dateDebutPenalite = dateDebutPenalite;
    }
    public LocalDate getDateFinPenalite() {
        return dateFinPenalite;
    }
    public void setDateFinPenalite(LocalDate dateFinPenalite) {
        this.dateFinPenalite = dateFinPenalite;
    }
    public boolean isActif() {
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

    
    
}
