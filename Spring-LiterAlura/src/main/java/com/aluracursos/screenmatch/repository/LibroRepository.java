package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Libro;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    List<Libro> findByIdiomaIgnoreCase(String idioma);

    List<Libro> findByNumeroDescargasGreaterThan(Integer cantidad);

    @Query("SELECT l FROM Libro l ORDER BY l.numeroDescargas DESC")
    List<Libro> topMasDescargados(Pageable pageable);

    @Query("""
           SELECT l 
           FROM Libro l 
           WHERE LOWER(l.autor.nombre) LIKE LOWER(CONCAT('%', :nombreAutor, '%'))
           """)
    List<Libro> buscarPorAutor(String nombreAutor);

    boolean existsByTituloIgnoreCaseAndAutor_NombreIgnoreCase(String titulo, String autorNombre);
}

