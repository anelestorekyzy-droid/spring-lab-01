package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService service;

    public BookRestController(BookService service) {
        this.service = service;
    }

    // GET - барлық кітаптарды көрсету
    @GetMapping
    public List<Book> list(
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "10") int limit) {

        return service.findAll(author)
                .stream()
                .limit(limit)
                .toList();
    }

    // GET - ID бойынша кітапты табу
    @GetMapping("/{id}")
    public ResponseEntity<Book> find(@PathVariable long id) {

        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    // POST - жаңа кітап қосу
    // Кітаптың иесі автоматты түрде анықталады
    @PostMapping
    public ResponseEntity<Book> create(
            @RequestBody Book book) {

        Book saved = service.create(book);

        return ResponseEntity
                .created(URI.create("/api/books/" + saved.id()))
                .body(saved);
    }

    // PUT - кітапты өзгерту
    // Тек иесі немесе ADMIN өзгерте алады
    @PutMapping("/{id}")
    public ResponseEntity<Book> replace(
            @PathVariable long id,
            @RequestBody Book book) {

        return service.update(id, book)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    // DELETE - кітапты өшіру
    // Тек иесі немесе ADMIN өшіре алады
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable long id) {

        boolean removed = service.delete(id);

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    // GET - кітап статистикасы
    @GetMapping("/stats")
    public ResponseEntity<BookStats> getStats() {

        List<Book> allBooks = service.findAll(null);

        if (allBooks.isEmpty()) {
            return ResponseEntity.ok(
                    new BookStats(0, 0, 0));
        }

        int count = allBooks.size();

        int earliestYear = allBooks.stream()
                .mapToInt(Book::year)
                .min()
                .orElse(0);

        int latestYear = allBooks.stream()
                .mapToInt(Book::year)
                .max()
                .orElse(0);

        return ResponseEntity.ok(
                new BookStats(
                        count,
                        earliestYear,
                        latestYear));
    }

    public record BookStats(
            int count,
            int earliestYear,
            int latestYear
    ) {}
}
