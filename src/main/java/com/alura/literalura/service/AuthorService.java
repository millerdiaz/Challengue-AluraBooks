package com.alura.literalura.service;

import com.alura.literalura.dto.AuthorDTO;
import com.alura.literalura.entity.Author;
import com.alura.literalura.entity.Book;
import com.alura.literalura.repository.AuthorRepository;
import com.alura.literalura.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    // Crear y guardar un autor
    @Transactional
    public Author createAuthor(String name, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book ID no encontrado: " + bookId));
        Author author = new Author(name, null, null, book); // Asigna valores predeterminados para birthYear y deathYear
        return authorRepository.save(author);
    }


    @Transactional
    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(author -> new AuthorDTO(author))  // Usa el constructor de AuthorDTO que acepta un Author
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AuthorDTO> getAuthorsByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book ID no encontrado: " + bookId));
        List<Author> authors = authorRepository.findByBook(book);
        return authors.stream()
                .map(author -> new AuthorDTO(author))  // Usamos AuthorDTO para mapear el Author
                .collect(Collectors.toList());
    }

    // Eliminar un autor por ID
    @Transactional
    public void deleteAuthorById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Author ID no encontrado: " + id));
        authorRepository.delete(author);
    }

    public List<AuthorDTO> getAuthorsAliveInYear(int year) {
        List<Author> authors = authorRepository.findAuthorsAliveInYear(year);
        return authors.stream()
                .map(AuthorDTO::new) // Convertir cada entidad `Author` a `AuthorDTO`
                .toList();
    }

    public AuthorDTO convertToDTO(Author author) {
        Integer birthYear = author.getBirth_year() != null ? author.getBirth_year(): -1; // Valor por defecto si es null
        Integer deathYear = author.getDeath_year() != null ? author.getDeath_year() : -1; // Valor por defecto si es null

        AuthorDTO authorDTO = new AuthorDTO(author);
        authorDTO.setBirth_year(birthYear);
        authorDTO.setDeath_year(deathYear);

        return authorDTO;
    }

}
