package io.github.gabrielmsouza.library.infrastructure.book.models;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.infrastructure.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;


@JacksonTest
class CreateBookRequestTest {

    @Autowired
    private JacksonTester<CreateBookRequest> json;

    @Test
    void testUnmarshall() throws Exception {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var json = """
        {
          "title": "%s",
          "author": "%s",
          "isbn": "%s"
        }
        """.formatted(expectedTitle, expectedAuthor, expectedIsbn);

        // when
        final var actualJson = this.json.parse(json);

        // then
        assertThat(actualJson)
            .hasFieldOrPropertyWithValue("title", expectedTitle)
            .hasFieldOrPropertyWithValue("author", expectedAuthor)
            .hasFieldOrPropertyWithValue("isbn", expectedIsbn);
    }
}