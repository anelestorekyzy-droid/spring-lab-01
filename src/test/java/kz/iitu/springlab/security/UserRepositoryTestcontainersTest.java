
package kz.iitu.springlab.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UserRepositoryTestcontainersTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("lab12_test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name",
                () -> "org.postgresql.Driver");
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUserInPostgres() {
        User user = new User();
        user.setUsername("anel_postgres");
        user.setPassword("password123");

        User saved = userRepository.saveAndFlush(user);

        assertNotNull(saved.getId());
        assertEquals("anel_postgres", saved.getUsername());
    }

    @Test
    void shouldFindUserByUsernameInPostgres() {
        User user = new User();
        user.setUsername("dariga_postgres");
        user.setPassword("password123");

        userRepository.saveAndFlush(user);

        User found = userRepository
                .findByUsername("dariga_postgres")
                .orElseThrow();

        assertEquals("dariga_postgres", found.getUsername());
    }
}
