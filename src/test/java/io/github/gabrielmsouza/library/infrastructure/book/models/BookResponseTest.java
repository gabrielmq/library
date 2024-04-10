package io.github.gabrielmsouza.library.infrastructure.book.models;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.infrastructure.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
class BookResponseTest {
    @Autowired
    private JacksonTester<BookResponse> json;

    @Test
    void testBookResponseMarshall() throws Exception {
        // given
        final var expectedId = Fixture.randomId();
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var response = new BookResponse(
            expectedId,
            expectedTitle,
            expectedAuthor,
            expectedIsbn
        );

        // when
        final var actualJson = this.json.write(response);

        // then
        assertThat(actualJson)
            .hasJsonPathValue("$.id", expectedId)
            .hasJsonPathValue("$.title", expectedTitle)
            .hasJsonPathValue("$.author", expectedAuthor)
            .hasJsonPathValue("$.isbn", expectedIsbn);
    }
}