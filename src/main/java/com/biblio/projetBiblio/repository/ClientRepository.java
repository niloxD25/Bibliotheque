package com.biblio.projetBiblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.biblio.projetBiblio.entity.*;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByNomIgnoreCase(String nom);
}
