package com.aluracursos.screenmatch.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String idioma;
    private Integer numeroDescargas;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro() { }

    public Libro(String titulo, Autor autor, String idioma, Integer numeroDescargas) {
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.numeroDescargas = numeroDescargas;
    }

    // Getters/Setters
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getIdioma() { return idioma; }
    public Integer getNumeroDescargas() { return numeroDescargas; }
    public Autor getAutor() { return autor; }
    public void setId(Long id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public void setNumeroDescargas(Integer numeroDescargas) { this.numeroDescargas = numeroDescargas; }
    public void setAutor(Autor autor) { this.autor = autor; }

    @Override
    public String toString() {
        String nombreAutor = (autor != null ? autor.getNombre() : "Desconocido");
        return "Libro{titulo='" + titulo + "', autor='" + nombreAutor + "', idioma='" +
                (idioma != null ? idioma : "desconocido") + "', descargas=" +
                (numeroDescargas != null ? numeroDescargas : 0) + "}";
    }
}
