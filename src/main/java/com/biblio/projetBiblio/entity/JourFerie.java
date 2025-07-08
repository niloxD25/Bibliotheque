package com.biblio.projetBiblio.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "jours_feries")
public class JourFerie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false, unique = true)
    private LocalDate date;

    
}

