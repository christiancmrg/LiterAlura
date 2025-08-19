package com.aluracursos.screenmatch.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer fechaNacimiento; // año
    private Integer fechaMuerte;     // año (null si vivo)

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private List<Libro> libros = new ArrayList<>();

    public Autor() { }

    public Autor(String nombre, Integer fechaNacimiento, Integer fechaMuerte) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaMuerte = fechaMuerte;
    }

    public void addLibro(Libro libro) {
        libros.add(libro);
        libro.setAutor(this);
    }

    // Getters/Setters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Integer getFechaNacimiento() { return fechaNacimiento; }
    public Integer getFechaMuerte() { return fechaMuerte; }
    public List<Libro> getLibros() { return libros; }
    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFechaNacimiento(Integer fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setFechaMuerte(Integer fechaMuerte) { this.fechaMuerte = fechaMuerte; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }

    @Override
    public String toString() {
        return nombre + " (" + (fechaNacimiento != null ? fechaNacimiento : "?")
                + " - " + (fechaMuerte != null ? fechaMuerte : "vivo") + ")";
    }
}
