📚 LiterAlura
📌 Descripción

Aplicación desarrollada en Java + Spring Boot que permite la búsqueda, registro y consulta de libros y autores utilizando la API de Gutendex. Los datos se almacenan en una base de datos PostgreSQL mediante Spring Data JPA.

🚀 Funcionalidades

✅ Búsqueda de libros por título en la API de Gutendex.
✅ Registro automático de libros en base de datos.
✅ Validación para evitar registros duplicados.
✅ Consulta de autores registrados con sus libros asociados.
✅ Listado de autores vivos en un año específico.
✅ Visualización en consola con formato mejorado (íconos y saltos de línea).

🎯 Funcionalidades Extras

✅ Mostrar información de un libro: título, autor, idioma y número de descargas.
✅ Mostrar información de un autor: nombre, año de nacimiento, año de muerte y todos sus libros registrados.
✅ Validación de entradas cuando un libro no existe en la API.
✅ Menú interactivo con opciones para navegar entre las funcionalidades.

🛠️ Tecnologías utilizadas

☕ Java 17+
🌱 Spring Boot 3.5+
💾 PostgreSQL (base de datos)
📦 Spring Data JPA (persistencia)
🌐 Gutendex API (fuente de datos de libros)
🔗 Jackson / ObjectMapper (mapeo JSON → objetos Java)

📂 Estructura del proyecto
LiterAlura/
│── src/
│   ├── main/java/com/aluracursos/screenmatch/
│   │   ├── LiterAluraApplication.java   # Punto de entrada del programa
│   │   ├── main/Principal.java          # Lógica principal y menú en consola
│   │   ├── model/Libro.java             # Entidad Libro
│   │   ├── model/Autor.java             # Entidad Autor
│   │   ├── repository/LibroRepository.java  # Repositorio para libros
│   │   ├── repository/AutorRepository.java  # Repositorio para autores
│   │   ├── services/ConsumoAPI.java     # Cliente HTTP para consumir Gutendex
│   │   ├── services/ConvierteDatos.java # Conversor JSON → Objetos
│   │   ├── model/DatosLibro.java        # DTO para libros
│   │   ├── model/DatosAutor.java        # DTO para autores
│── application.properties               # Configuración de conexión a BD
│── README.md                            # Documentación del proyecto

📖 Instrucciones de uso

Clona este repositorio o descarga los archivos.

Abre el proyecto en tu IDE favorito (IntelliJ, Eclipse, VS Code).

Configura en application.properties tu conexión a PostgreSQL:

spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update


Ejecuta la clase LiterAluraApplication.java.

Interactúa con el menú en consola para buscar libros, registrar autores y consultar información.

📷 Ejemplos de salida
🔍 Búsqueda de Libro
---- Libros 📖 ----
Título: Hamlet
Autor: William Shakespeare
Idioma: en
Número de descargas: 25340

👨‍🎨 Consulta de Autor
---- Autores 👨‍🎨 ----
Nombre: William Shakespeare
Año de nacimiento: 1564
Año de muerte: 1616
Libros:
- Hamlet
- Macbeth
- Romeo and Juliet

  ![BusquedaLiter](https://github.com/user-attachments/assets/53a7a883-cb5c-43f0-9ef4-b21fadeb8e2e)

![LibrosLiter](https://github.com/user-attachments/assets/41561591-a948-4349-9efc-96ba8010f7ef)


![AutoresLiter](https://github.com/user-attachments/assets/d41369a0-f94f-4d2f-aa53-2cb55192ba82)


⚠️ Libro duplicado
⚠️ El libro 'Hamlet' ya está registrado.

❌ Búsqueda sin resultados
Escribe el título: 78676453465
❌ No se pudo completar la búsqueda. Detalle: ❌ No se encontraron resultados en la búsqueda.

📌 Requisitos

✅ Java 17 o superior
✅ PostgreSQL instalado y corriendo
✅ Acceso a internet (para consultar la API Gutendex)

📌 Desarrollado por

#Christian Peralta Camargo
Inspirado en prácticas del programa de formación de #AluraLatam.
