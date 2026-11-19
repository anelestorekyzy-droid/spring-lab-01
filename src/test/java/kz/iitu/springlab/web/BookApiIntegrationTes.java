
package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.security.User;
import kz.iitu.springlab.security.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.flyway.enabled=true",
                "spring.jpa.hibernate.ddl-auto=validate"
        }
)
class BookApiIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("lab12_api")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateAndReadBookThroughRealHttp() {
        User user = new User();
        user.setUsername("anel_api");
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.saveAndFlush(user);

        String baseUrl = "http://localhost:" + port;
        RestTemplate rest = new RestTemplate();

        // 1. Login беті және сессия
        ResponseEntity<String> loginPage =
                rest.getForEntity(baseUrl + "/login", String.class);

        assertThat(loginPage.getStatusCode()).isEqualTo(HttpStatus.OK);

        String cookie = loginPage.getHeaders()
                .getFirst(HttpHeaders.SET_COOKIE);

        assertThat(cookie).isNotNull();

        String sessionCookie = cookie.split(";", 2)[0];

        // 2. Login бетінен CSRF токенін алу
        String html = loginPage.getBody();
        assertThat(html).isNotNull();

        java.util.regex.Matcher matcher =
                java.util.regex.Pattern.compile(
                        "name=\"_csrf\"[^>]*value=\"([^\"]+)\""
                ).matcher(html);

        assertThat(matcher.find()).isTrue();
        String csrf = matcher.group(1);

        // 3. Пайдаланушы ретінде кіру
        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.set(HttpHeaders.COOKIE, sessionCookie);
        loginHeaders.setContentType(
                MediaType.APPLICATION_FORM_URLENCODED);

        org.springframework.util.MultiValueMap<String, String> form =
                new org.springframework.util.LinkedMultiValueMap<>();

        form.add("username", "anel_api");
        form.add("password", "password123");
        form.add("_csrf", csrf);

        ResponseEntity<String> loginResponse =
                rest.exchange(
                        baseUrl + "/login",
                        HttpMethod.POST,
                        new HttpEntity<>(form, loginHeaders),
                        String.class
                );

        assertThat(loginResponse.getStatusCode().is3xxRedirection())
                .isTrue();

        String newCookie = loginResponse.getHeaders()
                .getFirst(HttpHeaders.SET_COOKIE);

        if (newCookie != null) {
            sessionCookie = newCookie.split(";", 2)[0];
        }

        // 4. Авторизацияланған сессиямен CSRF алу
        HttpHeaders pageHeaders = new HttpHeaders();
        pageHeaders.set(HttpHeaders.COOKIE, sessionCookie);

        ResponseEntity<String> booksPage =
                rest.exchange(
                        baseUrl + "/books",
                        HttpMethod.GET,
                        new HttpEntity<>(pageHeaders),
                        String.class
                );

        assertThat(booksPage.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 5. POST /api/books
        // CSRF токенін жаңарту үшін login бетін қайта ашамыз
        ResponseEntity<String> csrfPage =
                rest.exchange(
                        baseUrl + "/login",
                        HttpMethod.GET,
                        new HttpEntity<>(pageHeaders),
                        String.class
                );

        String csrfHtml = csrfPage.getBody();
        assertThat(csrfHtml).isNotNull();

        java.util.regex.Matcher csrfMatcher =
                java.util.regex.Pattern.compile(
                        "name=\"_csrf\"[^>]*value=\"([^\"]+)\""
                ).matcher(csrfHtml);

        assertThat(csrfMatcher.find()).isTrue();

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.set(HttpHeaders.COOKIE, sessionCookie);
        postHeaders.setContentType(MediaType.APPLICATION_JSON);
        postHeaders.set("X-CSRF-TOKEN", csrfMatcher.group(1));

        String json = """
                {
                  "title": "Lab 12 Test Book",
                  "author": "Anel",
                  "year": 2026
                }
                """;

        ResponseEntity<Book> created =
                rest.exchange(
                        baseUrl + "/api/books",
                        HttpMethod.POST,
                        new HttpEntity<>(json, postHeaders),
                        Book.class
                );

        assertThat(created.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        Book saved = created.getBody();
        assertThat(saved).isNotNull();
        assertThat(saved.id()).isNotNull();
        assertThat(saved.owner()).isEqualTo("anel_api");

        // 6. GET /api/books/{id}
        ResponseEntity<Book> fetched =
                rest.exchange(
                        baseUrl + "/api/books/" + saved.id(),
                        HttpMethod.GET,
                        new HttpEntity<>(pageHeaders),
                        Book.class
                );

        assertThat(fetched.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(fetched.getBody()).isNotNull();
        assertThat(fetched.getBody().title())
                .isEqualTo("Lab 12 Test Book");
    }
}
