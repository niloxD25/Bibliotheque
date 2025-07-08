package com.biblio.projetBiblio.repository;

import com.biblio.projetBiblio.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PretsEtatsRepository extends JpaRepository<PretsEtats, Long> {
    List<PretsEtats> findByPretIdOrderByDateEtatDesc(Long pretId);
}
