package com.biblio.projetBiblio.entity;

public class PretDTOO {
    private Pret pret;
    private Etat etatActuel;
    private boolean enRetard;

    public PretDTOO(Pret pret, Etat etatActuel) {
        this.pret = pret;
        this.etatActuel = etatActuel;
        this.enRetard = pret.isEnRetard();
    }

    public Pret getPret() {
        return pret;
    }

    public void setPret(Pret pret) {
        this.pret = pret;
    }

    public Etat getEtatActuel() {
        return etatActuel;
    }

    public void setEtatActuel(Etat etatActuel) {
        this.etatActuel = etatActuel;
    }

    public boolean isEnRetard() {
        return enRetard;
    }
}
