
package kz.iitu.springlab.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:lab12test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveUser() {
        User user = new User();
        user.setUsername("anel");
        user.setPassword("password123");

        User saved = userRepository.saveAndFlush(user);

        assertNotNull(saved.getId());
        assertEquals("anel", saved.getUsername());
        assertTrue(saved.isEnabled());
    }

    @Test
    void shouldFindUserByUsername() {
        User user = new User();
        user.setUsername("dariga");
        user.setPassword("password123");

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        User found = userRepository
                .findByUsername("dariga")
                .orElseThrow();

        assertEquals("dariga", found.getUsername());
        assertNotNull(found.getId());
    }

    @Test
    void shouldReturnEmptyForUnknownUsername() {
        User user = new User();
        user.setUsername("karakat");
        user.setPassword("password123");

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        assertTrue(
                userRepository.findByUsername("unknown").isEmpty()
        );
    }
}
