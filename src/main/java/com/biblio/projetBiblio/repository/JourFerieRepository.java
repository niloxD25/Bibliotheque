package com.biblio.projetBiblio.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.biblio.projetBiblio.entity.*;

public interface JourFerieRepository extends JpaRepository<JourFerie, Long> {
    @Query("SELECT jf.date FROM JourFerie jf")
    List<LocalDate> findAllDates();
}
