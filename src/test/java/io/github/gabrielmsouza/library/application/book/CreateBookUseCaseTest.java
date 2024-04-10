package io.github.gabrielmsouza.library.application.book;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.UseCaseTest;
import io.github.gabrielmsouza.library.application.book.CreateBookUseCase;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateBookUseCaseTest extends UseCaseTest {

    @InjectMocks
    private CreateBookUseCase createBookUseCase;

    @Mock
    private BookGateway bookGateway;

    @Captor
    private ArgumentCaptor<Book> captor;

    @Test
    void givenAValidParams_whenCallsCreateBook_thenShouldReturnBookId() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var input = new CreateBookUseCase.Input(expectedTitle, expectedAuthor, expectedIsbn);

        when(bookGateway.create(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.createBookUseCase.execute(input);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(bookGateway).create(captor.capture());

        final var actualBook = captor.getValue();
        assertEquals(expectedTitle, actualBook.getTitle());
        assertEquals(expectedAuthor, actualBook.getAuthor());
        assertEquals(expectedIsbn, actualBook.getIsbn());
    }

    @Test
    void givenAInvalidNullTitle_whenCallsCreateBook_thenShouldReturnNotificationException() {
        // given
        final String expectedTitle = null;
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var input = new CreateBookUseCase.Input(expectedTitle, expectedAuthor, expectedIsbn);

        when(bookGateway.existsByIsbn(expectedIsbn)).thenReturn(false);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.createBookUseCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmptyTitle_whenCallsCreateBook_thenShouldReturnNotificationException() {
        // given
        final var expectedTitle = "";
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var input = new CreateBookUseCase.Input(expectedTitle, expectedAuthor, expectedIsbn);

        when(bookGateway.existsByIsbn(expectedIsbn)).thenReturn(false);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.createBookUseCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidNullAuthor_whenCallsCreateBook_thenShouldReturnNotificationException() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final String expectedAuthor = null;
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorMessage = "'author' should not be empty";
        final var expectedErrorCount = 1;

        final var input = new CreateBookUseCase.Input(expectedTitle, expectedAuthor, expectedIsbn);

        when(bookGateway.existsByIsbn(expectedIsbn)).thenReturn(false);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.createBookUseCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmptyAuthor_whenCallsCreateBook_thenShouldReturnNotificationException() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = "";
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorMessage = "'author' should not be empty";
        final var expectedErrorCount = 1;

        final var input = new CreateBookUseCase.Input(expectedTitle, expectedAuthor, expectedIsbn);

        when(bookGateway.existsByIsbn(expectedIsbn)).thenReturn(false);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.createBookUseCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidNullIsbn_whenCallsCreateBook_thenShouldReturnNotificationException() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final String expectedIsbn = null;

        final var expectedErrorMessage = "'isbn' should not be empty";
        final var expectedErrorCount = 1;

        final var input = new CreateBookUseCase.Input(expectedTitle, expectedAuthor, expectedIsbn);

        when(bookGateway.existsByIsbn(expectedIsbn)).thenReturn(false);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.createBookUseCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmptyIsbn_whenCallsCreateBook_thenShouldReturnNotificationException() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = "";

        final var expectedErrorMessage = "'isbn' should not be empty";
        final var expectedErrorCount = 1;

        final var input = new CreateBookUseCase.Input(expectedTitle, expectedAuthor, expectedIsbn);

        when(bookGateway.existsByIsbn(expectedIsbn)).thenReturn(false);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.createBookUseCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAExistingBook_whenCallsCreateBook_thenShouldReturnNotificationException() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var input = new CreateBookUseCase.Input(expectedTitle, expectedAuthor, expectedIsbn);

        when(bookGateway.existsByIsbn(expectedIsbn)).thenReturn(true);

        final var expectedErrorMessage = "Already exists a book with isbn %s".formatted(expectedIsbn);
        final var expectedErrorCount = 1;

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.createBookUseCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }
}