package com.biblio.projetBiblio.dto;

import java.time.LocalDate;

public class PretDTO {
    private Long id;
    private String livreTitre;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLivreTitre() { return livreTitre; }
    public void setLivreTitre(String livreTitre) { this.livreTitre = livreTitre; }

    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public void setDateEmprunt(LocalDate dateEmprunt) { this.dateEmprunt = dateEmprunt; }

    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) { this.dateRetourPrevue = dateRetourPrevue; }

    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public void setDateRetourEffective(LocalDate dateRetourEffective) { this.dateRetourEffective = dateRetourEffective; }
}
