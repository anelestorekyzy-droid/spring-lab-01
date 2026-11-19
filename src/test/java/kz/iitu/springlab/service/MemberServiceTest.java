
package kz.iitu.springlab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;

class MemberServiceTest {

    private MemberService memberService;
    private Instant fixedNow;

    @BeforeEach
    void setUp() {
        fixedNow = Instant.parse("2026-11-19T12:00:00Z");

        Clock fixedClock = Clock.fixed(
                fixedNow,
                ZoneOffset.UTC
        );

        memberService = new MemberService(fixedClock);
    }

    @Test
    void shouldReturnTrueForActiveMembership() {
        Instant expiresAt = fixedNow.plus(1, ChronoUnit.DAYS);

        assertThat(memberService.isMembershipActive(expiresAt))
                .isTrue();
    }

    @Test
    void shouldReturnFalseForExpiredYesterday() {
        Instant expiresAt = fixedNow.minus(1, ChronoUnit.DAYS);

        assertThat(memberService.isMembershipActive(expiresAt))
                .isFalse();
    }

    @Test
    void shouldReturnFalseWhenExpiresNow() {
        assertThat(memberService.isMembershipActive(fixedNow))
                .isFalse();
    }

    @Test
    void shouldThrowExceptionForNullExpiration() {
        assertThatThrownBy(
                () -> memberService.isMembershipActive(null)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Expiration date cannot be null");
    }
}
