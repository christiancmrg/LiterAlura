package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("""
           SELECT a
           FROM Autor a
           WHERE a.fechaNacimiento <= :anio
             AND (a.fechaMuerte IS NULL OR a.fechaMuerte >= :anio)
           """)
    List<Autor> encontrarAutoresVivosEn(@Param("anio") int anio);

    List<Autor> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT DISTINCT a FROM Autor a LEFT JOIN FETCH a.libros")
    List<Autor> findAllConLibros();
}
