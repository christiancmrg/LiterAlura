package com.aluracursos.screenmatch.main;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.AutorRepository;
import com.aluracursos.screenmatch.repository.LibroRepository;
import com.aluracursos.screenmatch.services.consumoAPI;
import com.aluracursos.screenmatch.services.ConvierteDatos;

import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final consumoAPI consumo = new consumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();

    private static final String URL_BASE = "https://gutendex.com/books/?search=";

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("""
                    -------------------------------------
                    📚 Bienvenido a la Biblioteca Alura
                    -------------------------------------
                    1 - Buscar libro por Título (API → guardar)
                    2 - Listar libros de tu catálogo
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Top 5 libros más descargados (local DB)
                    7 - Top 5 libros más descargados (Gutendex)
                    8 - Buscar libro por tema (API)
                    0 - Salir
                    -------------------------------------
                    Ingresa una opción:
                    """);
            try {
                opcion = Integer.parseInt(teclado.nextLine());
                switch (opcion) {
                    case 1 -> buscarLibroPorTitulo();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosPorAnio();
                    case 5 -> listarLibrosPorIdioma();
                    case 6 -> top5LibrosDescargadosDB();
                    case 7 -> top5LibrosDescargadosGutendex();
                    case 8 -> buscarLibroPorTema();
                    case 0 -> System.out.println("👋 ¡Gracias por usar la biblioteca de Alura!");
                    default -> System.out.println("❌ Opción inválida. Intenta nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Opción inválida. Ingresa un número.");
            }
            System.out.println();
        }
    }

    // 1) Buscar libro por título → API → guardar en BD
    private void buscarLibroPorTitulo() {
        System.out.print("Escribe el título: ");
        var tituloBuscado = teclado.nextLine().trim();
        if (tituloBuscado.isEmpty()) {
            System.out.println("⚠️ El título no puede ir vacío.");
            return;
        }

        try {
            var url = URL_BASE + tituloBuscado.replace(" ", "+");
            var json = consumo.obtenerDatos(url);

            // 👇 Pedimos la lista completa y validamos vacío
            var lista = conversor.obtenerLista(json, "results", DatosLibro.class);
            if (lista == null || lista.isEmpty()) {
                System.out.println("❌ No se encontraron resultados para: " + tituloBuscado);
                return;
            }

            // Tomamos el primero
            var dto = lista.get(0);

            // Normalización
            String titulo = dto.titulo() != null ? dto.titulo().trim() : "(sin título)";
            String idioma = (dto.idiomas() != null && !dto.idiomas().isEmpty())
                    ? dto.idiomas().get(0).toLowerCase()
                    : "desconocido";
            Integer descargas = dto.numeroDeDescargas() != null ? dto.numeroDeDescargas() : 0;
            String nombreAutor = (dto.autores() != null && !dto.autores().isEmpty() && dto.autores().get(0).nombre() != null)
                    ? dto.autores().get(0).nombre().trim()
                    : "Autor desconocido";
            Integer anioNac = (dto.autores() != null && !dto.autores().isEmpty()) ? dto.autores().get(0).fechaNacimiento() : null;
            Integer anioMue = (dto.autores() != null && !dto.autores().isEmpty()) ? dto.autores().get(0).fechaMuerte() : null;

            // Duplicado (título + autor)
            if (libroRepository.existsByTituloIgnoreCaseAndAutor_NombreIgnoreCase(titulo, nombreAutor)) {
                System.out.println("ℹ️ El libro ya está registrado: " + titulo + " - " + nombreAutor);
                return;
            }

            // Autor existente o nuevo
            var autor = autorRepository.findByNombreContainingIgnoreCase(nombreAutor)
                    .stream().findFirst()
                    .orElseGet(() -> autorRepository.save(new Autor(nombreAutor, anioNac, anioMue)));

            // Guardar libro
            var libro = new Libro(titulo, autor, idioma, descargas);
            libroRepository.save(libro);
            System.out.println("✅ Guardado: " + libro);

        } catch (Exception e) {
            System.out.println("❌ No se pudo completar la búsqueda.");
        }
    }


    // 2) Listar libros
    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("---- 📚 Libros ----\n");
            for (var l : libros) {
                System.out.println("Título: " + l.getTitulo());
                System.out.println("Autor: " + (l.getAutor() != null ? l.getAutor().getNombre() : "Autor desconocido"));
                System.out.println("Idioma: " + l.getIdioma());
                System.out.println("Número de descargas: " + (l.getNumeroDescargas() != null ? l.getNumeroDescargas() : 0));
                System.out.println(); // salto de línea entre libros
            }
        }
    }

    // 3) Listar autores
    private void listarAutoresRegistrados() {
        var autores = autorRepository.findAllConLibros(); // usa el fetch join
        if (autores.isEmpty()) {
            System.out.println("⚠️ No hay autores registrados.");
            return;
        }

        System.out.println("---- 👤 Autores ----\n");
        for (var a : autores) {
            System.out.println("Nombre: " + a.getNombre());
            System.out.println("Año de nacimiento: " + (a.getFechaNacimiento() != null ? a.getFechaNacimiento() : "?"));
            System.out.println("Año de muerte: " + (a.getFechaMuerte() != null ? a.getFechaMuerte() : "vivo"));
            System.out.println("Libros:");
            if (a.getLibros() == null || a.getLibros().isEmpty()) {
                System.out.println(" - (sin libros registrados)");
            } else {
                a.getLibros().forEach(l -> System.out.println(" - " + l.getTitulo()));
            }
            System.out.println(); // separación entre autores
        }
    }


    // 4) Autores vivos en un año
    private void listarAutoresVivosPorAnio() {
        System.out.print("Ingresa el año a consultar: ");
        int anio = Integer.parseInt(teclado.nextLine());
        var vivos = autorRepository.encontrarAutoresVivosEn(anio);
        if (vivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en " + anio + ".");
        } else {
            vivos.forEach(a -> System.out.println("- " + a));
        }
    }

    // 5) Libros por idioma
    private void listarLibrosPorIdioma() {
        System.out.print("Ingresa el idioma (es, en, fr, de, ...): ");
        String idioma = teclado.nextLine().trim();
        var libros = libroRepository.findByIdiomaIgnoreCase(idioma);
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en el idioma: " + idioma);
        } else {
            libros.forEach(System.out::println);
        }
    }

    // 6) Top 5 (DB local)
    private void top5LibrosDescargadosDB() {
        var top5 = libroRepository.topMasDescargados(PageRequest.of(0, 5));
        if (top5.isEmpty()) {
            System.out.println("No hay libros en la base de datos.");
        } else {
            System.out.println("🏆 Top 5 (DB local):");
            top5.forEach(l -> System.out.println("- " + l));
        }
    }

    // 7) Top 5 (Gutendex)
    private void top5LibrosDescargadosGutendex() {
        String url = "https://gutendex.com/books/?ordering=-download_count";
        var json = consumo.obtenerDatos(url);
        List<DatosLibro> lista = conversor.obtenerLista(json, "results", DatosLibro.class);
        System.out.println("🏆 Top 5 (Gutendex):");
        lista.stream().limit(5).forEach(l -> {
            String autor = (l.autores() != null && !l.autores().isEmpty()) ? l.autores().get(0).nombre() : "Autor desconocido";
            String idioma = (l.idiomas() != null && !l.idiomas().isEmpty()) ? l.idiomas().get(0) : "desconocido";
            Integer d = l.numeroDeDescargas() != null ? l.numeroDeDescargas() : 0;
            System.out.println("- " + l.titulo() + " | " + autor + " | " + idioma + " | ⬇ " + d);
        });
    }

    // 8) Buscar por tema (solo mostrar resultados)
    private void buscarLibroPorTema() {
        System.out.print("Ingresa el tema a buscar: ");
        String tema = teclado.nextLine();
        String url = URL_BASE + tema.replace(" ", "+");
        var json = consumo.obtenerDatos(url);
        List<DatosLibro> lista = conversor.obtenerLista(json, "results", DatosLibro.class);
        if (lista.isEmpty()) {
            System.out.println("No se encontraron libros para el tema: " + tema);
        } else {
            lista.stream().limit(10).forEach(l -> {
                String autor = (l.autores() != null && !l.autores().isEmpty()) ? l.autores().get(0).nombre() : "Autor desconocido";
                String idioma = (l.idiomas() != null && !l.idiomas().isEmpty()) ? l.idiomas().get(0) : "desconocido";
                Integer d = l.numeroDeDescargas() != null ? l.numeroDeDescargas() : 0;
                System.out.println("- " + l.titulo() + " | " + autor + " | " + idioma + " | ⬇ " + d);
            });
        }
    }
}
