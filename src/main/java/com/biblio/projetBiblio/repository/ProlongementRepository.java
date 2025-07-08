package com.biblio.projetBiblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.biblio.projetBiblio.entity.*;

import java.util.List;

public interface ProlongementRepository extends JpaRepository<Prolongement, Long> {
    List<Prolongement> findByPretIdAndActifTrue(Long pretId);
    List<Prolongement> findByPretClientIdOrderByDateDemandeDesc(Long clientId);

    @Query("""
        SELECT COUNT(p) 
        FROM Prolongement p
        WHERE p.pret.client.id = :clientId
        AND p.actif = true
    """)
    long countActifsForClient(@Param("clientId") Long clientId);

}
