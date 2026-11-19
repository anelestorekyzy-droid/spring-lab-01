
package kz.iitu.springlab.security;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserRepositoryMockitoTest {

    @Test
    void shouldCaptureUsername() {
        UserRepository repository = mock(UserRepository.class);

        String username = "anel";

        repository.findByUsername(username);

        ArgumentCaptor<String> captor =
                ArgumentCaptor.forClass(String.class);

        verify(repository).findByUsername(captor.capture());

        assertThat(captor.getValue()).isEqualTo("anel");
    }
}
