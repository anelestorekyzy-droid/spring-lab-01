
package kz.iitu.springlab.catalog;

public record Book(
        Long id,
        String title,
        String author,
        int year,
        String owner
) {
    // Compatibility with existing Lab 9 code
    public Book(Long id, String title, String author, int year) {
        this(id, title, author, year, null);
    }
}
