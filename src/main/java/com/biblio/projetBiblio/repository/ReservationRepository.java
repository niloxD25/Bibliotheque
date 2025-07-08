package com.biblio.projetBiblio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.biblio.projetBiblio.entity.*;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    int countByClientIdAndActifTrue(Long clientId);
    List<Reservation> findByClientId(Long clientId);
}

