---
  name: java-unit-test
  description: Write JUnit 5 + Mockito unit tests for a class in this Spring Boot project. Use when the user asks to add or write unit tests for a Java class.
  ---

  # Writing unit tests

  1. Read the target class and an existing test in the same package to match style.
  2. Create `src/test/java/<same package>/<ClassName>Test.java`.
  3. Conventions:
     - `@ExtendWith(MockitoExtension.class)`
     - Dependencies as `@Mock`, class under test as `@InjectMocks`
     - No reflection (`ReflectionTestUtils`, etc.)
     - Build DTOs with record constructors and fixed `LocalDateTime.of(...)` values
     - Test names start with `should...`
     - Compare `BigDecimal` with `compareTo`, not `equals`, when scale can differ
     - use AssertThat from Hamcrest
     - Use jUnit5
  4. Run `mvn test -Dtest=<ClassName>Test`, then `mvn test`, and report the results.