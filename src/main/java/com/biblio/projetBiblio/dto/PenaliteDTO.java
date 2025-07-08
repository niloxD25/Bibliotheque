package com.biblio.projetBiblio.dto;

import java.time.LocalDate;

public class PenaliteDTO {
    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String raison;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getRaison() { return raison; }
    public void setRaison(String raison) { this.raison = raison; }
}
