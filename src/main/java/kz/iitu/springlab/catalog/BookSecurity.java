
package kz.iitu.springlab.catalog;

import org.springframework.stereotype.Component;

@Component("bookSecurity")
public class BookSecurity {

    private final BookRepository repository;

    public BookSecurity(BookRepository repository) {
        this.repository = repository;
    }

    public boolean isOwner(long bookId, String username) {
        return repository.findById(bookId)
                .map(book -> username.equals(book.owner()))
                .orElse(false);
    }
}
