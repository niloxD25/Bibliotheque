package com.biblio.projetBiblio.repository;

import com.biblio.projetBiblio.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface EtatsRepository extends JpaRepository<Etat, Long> {
    @Query("SELECT e FROM Etat e WHERE LOWER(e.nom) = LOWER(:nom)")
    Optional<Etat> findByNomIgnoreCase(@Param("nom") String nom);
}
