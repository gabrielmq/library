package io.github.gabrielmsouza.library.infrastructure;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@DataJpaTest
@Target(TYPE)
@Retention(RUNTIME)
@Tag("integrationTest")
@ActiveProfiles("test-integration")
@ExtendWith(H2CleanUpExtension.class)
@ComponentScan(
        basePackages = "io.github.gabrielmsouza.library",
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*H2Gateway")
        }
)
public @interface H2GatewayTest {
}
