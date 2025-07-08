package com.biblio.projetBiblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.biblio.projetBiblio.entity.*;
import java.util.Optional;

public interface ReservationStatutRepository extends JpaRepository<ReservationStatut, Long> {
    Optional<ReservationStatut> findByNomIgnoreCase(String nom);
}

