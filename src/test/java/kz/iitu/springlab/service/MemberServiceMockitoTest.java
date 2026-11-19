
package kz.iitu.springlab.service;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MemberServiceMockitoTest {

    @Test
    void shouldCheckMembershipUsingMockedClock() {
        Clock clock = mock(Clock.class);

        Instant fixedNow =
                Instant.parse("2026-11-19T12:00:00Z");

        when(clock.instant()).thenReturn(fixedNow);
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);

        MemberService service = new MemberService(clock);

        Instant expiresAt =
                Instant.parse("2026-11-20T12:00:00Z");

        assertThat(service.isMembershipActive(expiresAt))
                .isTrue();

        verify(clock).instant();
    }

    @Test
    void shouldReturnFalseForExpiredYesterdayUsingMockito() {
        Clock clock = mock(Clock.class);

        Instant fixedNow =
                Instant.parse("2026-11-19T12:00:00Z");

        when(clock.instant()).thenReturn(fixedNow);

        MemberService service = new MemberService(clock);

        Instant expiresAt =
                fixedNow.minus(1, ChronoUnit.DAYS);

        assertThat(service.isMembershipActive(expiresAt))
                .isFalse();

        verify(clock, times(1)).instant();
    }
}
