package io.github.gabrielmsouza.library.domain.book;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.domain.UnitTest;
import io.github.gabrielmsouza.library.domain.exceptions.DomainException;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest extends UnitTest {

    @Test
    void givenAValidParams_whenCallsCreateBook_thenShouldInstantiateABook() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        // when
        final var actualBook = Book.with(expectedTitle, expectedAuthor, expectedIsbn);

        // then
        assertNotNull(actualBook.getId());
        assertEquals(expectedTitle, actualBook.getTitle());
        assertEquals(expectedAuthor, actualBook.getAuthor());
        assertEquals(expectedIsbn, actualBook.getIsbn());
    }

    @Test
    void givenAInvalidNullName_whenCallsCreateBook_thenShouldReceiveANotification() {
        // given
        final String expectedTitle = null;
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        // when
        final var actualException =
                assertThrows(NotificationException.class, () -> Book.with(expectedTitle, expectedAuthor, expectedIsbn));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmptyName_whenCallsCreateBook_thenShouldReceiveANotification() {
        // given
        final var expectedTitle = "";
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        // when
        final var actualException =
            assertThrows(NotificationException.class, () -> Book.with(expectedTitle, expectedAuthor, expectedIsbn));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidNullAuthor_whenCallsCreateBook_thenShouldReceiveANotification() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final String expectedAuthor = null;
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'author' should not be empty";

        // when
        final var actualException =
            assertThrows(NotificationException.class, () -> Book.with(expectedTitle, expectedAuthor, expectedIsbn));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmptyAuthor_whenCallsCreateBook_thenShouldReceiveANotification() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = "";
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'author' should not be empty";

        // when
        final var actualException =
                assertThrows(NotificationException.class, () -> Book.with(expectedTitle, expectedAuthor, expectedIsbn));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidIsbnAuthor_whenCallsCreateBook_thenShouldReceiveANotification() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final String expectedAuthor = null;
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'author' should not be empty";

        // when
        final var actualException =
                assertThrows(NotificationException.class, () -> Book.with(expectedTitle, expectedAuthor, expectedIsbn));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmptyIsbn_whenCallsCreateBook_thenShouldReceiveANotification() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = "";

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'isbn' should not be empty";

        // when
        final var actualException =
                assertThrows(NotificationException.class, () -> Book.with(expectedTitle, expectedAuthor, expectedIsbn));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidNullIsbn_whenCallsCreateBook_thenShouldReceiveANotification() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final String expectedIsbn = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'isbn' should not be empty";

        // when
        final var actualException =
                assertThrows(NotificationException.class, () -> Book.with(expectedTitle, expectedAuthor, expectedIsbn));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAValidBook_whenCallsUpdate_thenShouldUpdateIt() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var book = Book.with("Title", "Author", "Isbn");

        // when
        final var actualBook = book.update(expectedTitle, expectedAuthor, expectedIsbn);

        // then
        assertEquals(book.getId(), actualBook.getId());
        assertEquals(expectedTitle, actualBook.getTitle());
        assertEquals(expectedAuthor, actualBook.getAuthor());
        assertEquals(expectedIsbn, actualBook.getIsbn());
    }

    @Test
    void givenAValidBook_whenCallsUpdateWithInvalidNullTitle_thenShouldReceiveNotification() {
        // given
        final String expectedTitle = null;
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        final var book = Book.with("Title", "Author", "Isbn");
        assertNotNull(book);
        assertNotNull(book.getId());

        // when
        final var actualException =
            assertThrows(
                NotificationException.class,
                () -> book.update(expectedTitle, expectedAuthor, expectedIsbn)
            );

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAValidBook_whenCallsUpdateWithInvalidEmptyTitle_thenShouldReceiveNotification() {
        // given
        final var expectedTitle = "";
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        final var book = Book.with("Title", "Author", "Isbn");
        assertNotNull(book);
        assertNotNull(book.getId());

        // when
        final var actualException =
            assertThrows(
                NotificationException.class,
                () -> book.update(expectedTitle, expectedAuthor, expectedIsbn)
            );

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAValidBook_whenCallsUpdateWithInvalidNullAuthor_thenShouldReceiveNotification() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final String expectedAuthor = null;
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'author' should not be empty";

        final var book = Book.with("Title", "Author", "Isbn");
        assertNotNull(book);
        assertNotNull(book.getId());

        // when
        final var actualException =
            assertThrows(
                NotificationException.class,
                () -> book.update(expectedTitle, expectedAuthor, expectedIsbn)
            );

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAValidBook_whenCallsUpdateWithInvalidEmptyAuthor_thenShouldReceiveNotification() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = "";
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'author' should not be empty";

        final var book = Book.with("Title", "Author", "Isbn");
        assertNotNull(book);
        assertNotNull(book.getId());

        // when
        final var actualException =
            assertThrows(
                NotificationException.class,
                () -> book.update(expectedTitle, expectedAuthor, expectedIsbn)
            );

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAValidBook_whenCallsUpdateWithInvalidNullIsbn_thenShouldReceiveNotification() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final String expectedIsbn = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'isbn' should not be empty";

        final var book = Book.with("Title", "Author", "Isbn");
        assertNotNull(book);
        assertNotNull(book.getId());

        // when
        final var actualException =
            assertThrows(
                NotificationException.class,
                () -> book.update(expectedTitle, expectedAuthor, expectedIsbn)
            );

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAValidBook_whenCallsUpdateWithInvalidEmptyIsbn_thenShouldReceiveNotification() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = "";

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'isbn' should not be empty";

        final var book = Book.with("Title", "Author", "Isbn");
        assertNotNull(book);
        assertNotNull(book.getId());

        // when
        final var actualException =
            assertThrows(
                NotificationException.class,
                () -> book.update(expectedTitle, expectedAuthor, expectedIsbn)
            );

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }
}