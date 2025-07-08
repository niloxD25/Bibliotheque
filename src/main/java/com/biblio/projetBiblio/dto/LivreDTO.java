package com.biblio.projetBiblio.dto;

import java.util.List;

public class LivreDTO {
    private Long id;
    private String titre;
    private String auteur;
    private String editeur;
    private Integer age;
    private String genre;
    private List<ExemplaireDTO> exemplaires;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public String getEditeur() { return editeur; }
    public void setEditeur(String editeur) { this.editeur = editeur; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public List<ExemplaireDTO> getExemplaires() { return exemplaires; }
    public void setExemplaires(List<ExemplaireDTO> exemplaires) { this.exemplaires = exemplaires; }
}
