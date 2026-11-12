package kz.iitu.springlab.catalog;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll(String author) {
        List<Book> allBooks = repository.findAll();

        if (author == null || author.isBlank()) {
            return allBooks;
        }

        return allBooks.stream()
                .filter(book -> book.author().toLowerCase()
                        .contains(author.toLowerCase()))
                .toList();
    }

    public Optional<Book> findById(long id) {
        return repository.findById(id);
    }

    public Book create(Book book) {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        Book newBook = new Book(
                null,
                book.title(),
                book.author(),
                book.year(),
                username
        );

        return repository.save(newBook);
    }

    @PreAuthorize("hasRole('ADMIN') or @bookSecurity.isOwner(#p0, authentication.name)")
    public Optional<Book> update(long id, Book book) {

        return repository.findById(id).map(existing -> {
            Book updated = new Book(
                    id,
                    book.title(),
                    book.author(),
                    book.year(),
                    existing.owner()
            );

            return repository.save(updated);
        });
    }

    @PreAuthorize("hasRole('ADMIN') or @bookSecurity.isOwner(#p0, authentication.name)")
    public boolean delete(long id) {
        return repository.deleteById(id);
    }
}
