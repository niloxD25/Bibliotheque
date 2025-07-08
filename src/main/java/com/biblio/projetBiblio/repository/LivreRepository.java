package com.biblio.projetBiblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.biblio.projetBiblio.entity.*;

public interface LivreRepository extends JpaRepository<Livre, Long> {
}
