package com.biblio.projetBiblio.repository;

import com.biblio.projetBiblio.entity.Pret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PretRepository extends JpaRepository<Pret, Long> {

    List<Pret> findByClientId(Long clientId);

    List<Pret> findByExemplaireId(Long exemplaireId);

    List<Pret> findByDateRetourEffectiveIsNull();

    long countByClientIdAndDateRetourEffectiveIsNull(Long clientId);
}
