
package kz.iitu.springlab.catalog;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record Book(
        Long id,

        @NotBlank(message = "Title cannot be blank")
        String title,

        @NotBlank(message = "Author cannot be blank")
        String author,

        @Min(value = 1000, message = "Year must be at least 1000")
        @Max(value = 2100, message = "Year must not exceed 2100")
        int year,

        String owner
) {
    public Book(Long id, String title, String author, int year) {
        this(id, title, author, year, null);
    }
}
