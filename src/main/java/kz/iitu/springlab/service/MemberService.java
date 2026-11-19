package kz.iitu.springlab.service;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
public class MemberService {

    private final Clock clock;

    public MemberService(Clock clock) {
        this.clock = clock;
    }

    public boolean isMembershipActive(Instant expiresAt) {
        if (expiresAt == null) {
            throw new IllegalArgumentException(
                    "Expiration date cannot be null"
            );
        }

        return expiresAt.isAfter(Instant.now(clock));
    }
}
