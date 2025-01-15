package com.alura.literalura.principal;

import com.alura.literalura.dto.AuthorDTO;
import com.alura.literalura.dto.BookDTO;
import com.alura.literalura.dto.GutendexResponse;
import com.alura.literalura.entity.Author;
import com.alura.literalura.service.AuthorService;
import com.alura.literalura.service.BookService;
import com.alura.literalura.service.GutendexService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class PrincipalApp implements CommandLineRunner {

    private final GutendexService gutendexService;
    private final BookService bookService;
    private final AuthorService authorService; // Inyección de AuthorService


    public PrincipalApp(GutendexService gutendexService, BookService bookService, AuthorService authorService) {
        this.gutendexService = gutendexService;
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showMenu();
            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1 -> findBooks(scanner); // searchBooks
                case 2 -> displayBooks(); // showBooks
                case 3 -> removeBook(scanner); // deleteBook
                case 4 -> listAuthorsByBook(scanner); // showAuthorsByBook
                case 5 -> listAllAuthors(); // showAllAuthors
                case 6 -> displayBooksByLanguage(scanner); // showBooksByLanguage
                case 7 -> listAuthorsAliveInYear(scanner); // showAuthorsAliveInYear
                case 0 -> quit(); // exit
                default -> System.out.println("Opción no válida.");
            }


        }
    }

    // Mostrar el menú de opciones
    private void showMenu() {
        System.out.println("Bienvenido a nuestro menu de opciones de nuestra libreria: ");
        System.out.println("1. Buscar un libro por título");
        System.out.println("2. Ver la lista de libros guardados");
        System.out.println("3. Eliminar un libro por ID");
        System.out.println("4. Consultar la lista de autores registrados");
        System.out.println("5. Mostrar todos los autores");
        System.out.println("6. Mostrar la cantidad de libros por idioma");
        System.out.println("7. Listar autores vivos en un año específico");
        System.out.println("0. Salir\n");
        System.out.print("Seleccione una opción: ");
    }


    // Obtener una opción válida del usuario
    private int getUserChoice(Scanner scanner) {
        int choice = -1;
        boolean validChoice = false;
        while (!validChoice) {
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume el salto de línea
                validChoice = true; // Si no se lanza una excepción, la opción es válida
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
        }
        return choice;
    }

    // Método para buscar libros y mostrar resultados
    private void findBooks(Scanner scanner) {
        System.out.print("¿Cual es el libro que estas buscando? ");
        String query = scanner.nextLine();
        List<GutendexResponse.Book> books = gutendexService.searchBooksByTitle(query);

        // Usar el método displayBooks para mostrar los resultados
        displayBooks(books);

        if (books != null && !books.isEmpty()) {
            System.out.print("¿Te gustaria guardar alguno de los libros ?(y/n): ");
            String saveChoice = scanner.nextLine();
            if (saveChoice.equalsIgnoreCase("y")) {
                System.out.print("Ingresa la opción del libro que quieres guardar: ");
                try {
                    int bookIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    if (bookIndex >= 0 && bookIndex < books.size()) {
                        gutendexService.storeBook(books.get(bookIndex));
                    } else {
                        System.out.println("Opción inválida. Ingresa una opción válida.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Opción no válida, ingresa una opción válida.");
                }
            }
        }
    }

    // Mostrar libros guardados con formato
    @Transactional
    public void displayBooks() {
        List<BookDTO> books = bookService.getAllBooksDTO();
        if (books.isEmpty()) {
            System.out.println("No existen libros guardados.");
        } else {
            for (BookDTO book : books) {
                System.out.println("----Libro------");
                System.out.println("Titulo: " + book.getTitle());
                System.out.println("Autor: " + book.getAuthors());
                System.out.println("Idioma: " + book.getLanguage());
                System.out.println("Numero de descargas: " + book.getDownloadCount());
                System.out.println("ID libro: " + book.getId());
                System.out.println("----------------");
            }
        }
    }

    // Eliminar un libro
    private void removeBook(Scanner scanner) {
        System.out.print("Ingresa el ID del libro que quieres eliminar: ");
        Long id = null;
        boolean validId = false;
        while (!validId) {
            try {
                id = scanner.nextLong();
                scanner.nextLine(); // Consume el salto de línea
                validId = true;
            } catch (InputMismatchException e) {
                System.out.println("opción no válida, ingresa un número válido para el ID.");
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
        }
        bookService.deleteBookById(id);
        System.out.println("Libro eliminado con exito.");
    }

    // Mostrar autores por libro
    @Transactional
    public void listAuthorsByBook(Scanner scanner) {
        System.out.print("Ingresa el ID del libro: ");
        Long bookId = getValidLong(scanner);
        List<AuthorDTO> authors = authorService.getAuthorsByBookId(bookId);

        if (authors.isEmpty()) {
            System.out.println("No se encontró ningún autor para el libro con ID " + bookId);
        } else {
            System.out.println("\nAutores para el libro con ID " + bookId + ":");
            System.out.printf("%-5s %-30s %-20s %-10s%n", "ID", "Nombre", "Fecha de Nacimiento", "Fecha de Muerte"); // Encabezados de columna
            System.out.println("--------------------------------------------------------------");

            authors.forEach(author -> {
                // Formateo de la salida para los detalles del autor
                System.out.printf("%-5d %-30s %-20s %-10s%n",
                        author.getId(),
                        author.getName(),
                        (author.getBirth_year() != null ? author.getBirth_year() : "Desconocida"),
                        (author.getDeath_year() != null ? author.getDeath_year() : "Desconocida"));
            });
        }
    }


    // Mostrar todos los autores
    @Transactional
    public void listAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthors();
        if (authors.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            System.out.println("Lista de autores registrados:\n");

            // Imprimir encabezados
            System.out.printf("%-30s %-20s %-20s %-10s%n", "Nombre Autor", "Fecha de Nacimiento", "Fecha de Muerte", "Vivo");

            // Imprimir cada autor con su detalle
            authors.forEach(author -> {
                String deathYear = (author.getDeath_year() != null) ? String.valueOf(author.getDeath_year()) : "Vivo";
                System.out.printf("%-30s %-20s %-20s %-10s%n",
                        author.getName(),
                        author.getBirth_year(),
                        deathYear,
                        (author.getDeath_year() == null) ? "Sí" : "No");
            });
        }
    }


    // Salir de la aplicación
    private void quit() {
        System.out.println("Saliendo, gracias por tu visita.");
        System.exit(0);
    }





    private void displayBooks(List<GutendexResponse.Book> books) {
        if (books == null || books.isEmpty()) {
            System.out.println("No se encontraron libros con tu búsqueda.");
            return;
        }

        System.out.println("Libros encontrados:");
        for (int i = 0; i < books.size(); i++) {
            GutendexResponse.Book book = books.get(i);

            String authors = (book.getAuthors() != null && !book.getAuthors().isEmpty())
                    ? book.getAuthors().stream()
                    .map(author -> author.getName() + " (" + author.getBirth_year() + " - " + author.getDeath_year() + ")")
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Sin autor")
                    : "Sin autor";

            System.out.printf("%d. %s por %s%n", i + 1, book.getTitle(), authors);


            for (GutendexResponse.Author author : book.getAuthors()) {
                // Convertir GutendexResponse.Author a Author (com.alura.literalura.entity.Author)
                Author entityAuthor = new Author(
                        author.getName(),    // Nombre del autor
                        author.getBirth_year(),  // Año de nacimiento
                        author.getDeath_year(),  // Año de muerte
                        null                 // Libro asociado (no se especifica aquí)
                );

                AuthorDTO authorDTO = new AuthorDTO(entityAuthor);  // Crear AuthorDTO con la entidad Author
                System.out.println(authorDTO); // Imprime el AuthorDTO con los detalles del autor
            }

        }
    }

    // Método auxiliar para obtener un número válido
    private Long getValidLong(Scanner scanner) {
        Long value = null;
        boolean valid = false;
        while (!valid) {
            try {
                value = scanner.nextLong();
                scanner.nextLine(); // Consumir el salto de línea
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número válido.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }
        return value;
    }



    // Mostrar cantidad de libros por idioma
    private void displayBooksByLanguage(Scanner scanner) {
        System.out.print("Ingrese los idiomas separados por coma (por ejemplo, 'en,es,fr'): ");
        String input = scanner.nextLine();
        String[] languages = input.split(",");

        if (languages.length < 1) {
            System.out.println("Debe ingresar al menos un idioma.");
            return;
        }

        for (String language : languages) {
            String trimmedLanguage = language.trim();
            long count = bookService.countBooksByLanguage(trimmedLanguage);
            System.out.printf("Cantidad de libros en '%s': %d%n", trimmedLanguage, count);
        }
    }

    // Mostrar autores vivos en un año específico
    private void listAuthorsAliveInYear(Scanner scanner) {
        System.out.print("Ingrese el año para buscar autores vivos: ");
        int year;
        try {
            year = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un año válido.");
            return;
        }

        List<AuthorDTO> authors = authorService.getAuthorsAliveInYear(year);
        if (authors.isEmpty()) {
            System.out.printf("No se encontraron autores vivos en el año %d.%n", year);
        } else {
            System.out.printf("\nAutores vivos en el año %d:%n", year);
            // Imprime los encabezados
            System.out.printf("%-5s %-30s %-20s %-10s%n", "ID", "Nombre", "Fecha de Nacimiento", "Fecha de Muerte");
            System.out.println("---------------------------------------------------------------");

            // Imprime la lista de autores
            authors.forEach(author -> {
                System.out.printf("%-5d %-30s %-20s %-10s%n",
                        author.getId(),
                        author.getName(),
                        (author.getBirth_year() != null ? author.getBirth_year() : "Desconocida"),
                        (author.getDeath_year() != null ? author.getDeath_year() : "Desconocida"));
            });
        }
    }


}