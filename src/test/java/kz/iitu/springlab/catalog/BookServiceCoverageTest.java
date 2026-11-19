
package kz.iitu.springlab.catalog;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookServiceCoverageTest {

    private final BookRepository repository =
            mock(BookRepository.class);

    private final BookService service =
            new BookService(repository);

    @Test
    void shouldReturnAllBooksWhenAuthorIsNull() {
        when(repository.findAll()).thenReturn(List.of(
                new Book(1L, "Clean Code",
                        "Robert Martin", 2008),
                new Book(2L, "Effective Java",
                        "Joshua Bloch", 2018)
        ));

        assertThat(service.findAll(null)).hasSize(2);
        verify(repository).findAll();
    }

    @Test
    void shouldFilterBooksByAuthor() {
        when(repository.findAll()).thenReturn(List.of(
                new Book(1L, "Clean Code",
                        "Robert Martin", 2008),
                new Book(2L, "Effective Java",
                        "Joshua Bloch", 2018)
        ));

        List<Book> result = service.findAll("bloch");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title())
                .isEqualTo("Effective Java");
    }

    @Test
    void shouldReturnAllBooksWhenAuthorIsBlank() {
        when(repository.findAll()).thenReturn(List.of(
                new Book(1L, "Clean Code",
                        "Robert Martin", 2008)
        ));

        assertThat(service.findAll("   ")).hasSize(1);
    }
}
