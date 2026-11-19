package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService service;

    @Test
    @WithMockUser
    void shouldReturn200ForExistingBook() throws Exception {
        Book book = new Book(
                1L, "Clean Code", "Robert Martin", 2008);

        when(service.findById(1L))
                .thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Clean Code"));
    }

    @Test
    @WithMockUser
    void shouldReturn404ForMissingBook() throws Exception {
        when(service.findById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title")
                        .value("Book Not Found"))
                .andExpect(jsonPath("$.status")
                        .value(404));
    }

    @Test
    @WithMockUser
    void shouldReturn400ForInvalidBook() throws Exception {
        String invalidJson = """
                {
                  "title": "",
                  "author": "",
                  "year": 500
                }
                """;

        mockMvc.perform(post("/api/books")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(service, never()).create(any());
    }
}
