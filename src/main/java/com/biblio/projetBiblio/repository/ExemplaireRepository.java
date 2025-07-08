package com.biblio.projetBiblio.repository;

import com.biblio.projetBiblio.entity.Exemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
    List<Exemplaire> findByLivreIdAndDisponibleTrue(Long livreId);
    List<Exemplaire> findByLivreId(Long livreId);
}
