
# Lab 12 — Automated Testing Evidence

Student: Nursalibidinova Anel
Group: 2401-IS

## Task 1 — Unit Testing
- JUnit 5, Mockito and AssertJ
- MemberService tests with fixed Clock
- Membership expired yesterday scenario

## Task 2 — Web Layer Testing
- MockMvc tests for BookRestController
- HTTP 200, 404 and 400 scenarios
- Spring Security test authentication

## Task 3 — Data Layer Testing
- DataJpaTest and TestEntityManager
- flush() and clear()
- Positive and negative repository queries
- 3 tests passed

## Task 4 — Integration Testing
- PostgreSQL Testcontainers
- Flyway V1 and V2 migrations
- Spring Security authentication and CSRF
- Real HTTP POST and GET requests
- Integration test passed

## Task 5 — JaCoCo Coverage

BookService coverage:

| Metric | Before | After |
|---|---|---|
| Instruction | 45% | 63% |
| Branch | 25% | 100% |

Additional tests covered null, blank and filtered author cases.

Remaining uncovered methods:
- update()
- delete()

## Task 6 — Build Evidence
- Maven clean verify: BUILD SUCCESS
- JaCoCo HTML report generated
- Test failures: 0
- Test errors: 0
