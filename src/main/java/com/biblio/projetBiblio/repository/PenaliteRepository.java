package com.biblio.projetBiblio.repository;

import com.biblio.projetBiblio.entity.Penalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PenaliteRepository extends JpaRepository<Penalite, Long> {

    /**
     * Vérifie si le client est pénalisé à la date donnée.
     *
     * @param clientId ID du client
     * @param today    Date actuelle
     * @return true si le client a une pénalité active ce jour-là
     */
    @Query("""
        SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
        FROM Penalite p
        WHERE p.client.id = :clientId
          AND :today BETWEEN p.dateDebut AND p.dateFin
    """)
    boolean isClientPenalised(@Param("clientId") Long clientId, @Param("today") LocalDate today);

    Optional<Penalite> findTopByClientIdAndDateFinGreaterThanEqualOrderByDateFinDesc(Long clientId, LocalDate date);

}
