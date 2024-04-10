package io.github.gabrielmsouza.library.application.book;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.UseCaseTest;
import io.github.gabrielmsouza.library.application.book.UpdateBookUseCase.Input;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.infrastructure.book.models.UpdateBookRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static io.github.gabrielmsouza.library.application.book.UpdateBookUseCase.Input.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateBookUseCaseTest extends UseCaseTest {
    @InjectMocks
    private UpdateBookUseCase useCase;

    @Mock
    private BookGateway gateway;

    @Captor
    private ArgumentCaptor<Book> captor;

    @Test
    void givenAValidParams_whenCallsUpdateBook_thenShouldReturnItsIdentifier() {
        // given
        final var book = Book.with("title", "author", "isbn");

        final var expectedId = book.getId();
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var input = from(expectedId, new UpdateBookRequest(expectedTitle, expectedAuthor, expectedIsbn));

        when(gateway.findById(expectedId)).thenReturn(Optional.of(Book.with(book)));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(input);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId, actualOutput.id());

        verify(gateway).findById(expectedId);
        verify(gateway).update(captor.capture());

        final var actualBook = captor.getValue();
        assertEquals(expectedId, actualBook.getId());
        assertEquals(expectedTitle, actualBook.getTitle());
        assertEquals(expectedAuthor, actualBook.getAuthor());
        assertEquals(expectedIsbn, actualBook.getIsbn());
    }

    @Test
    void givenAInvalidTitle_whenCallsUpdateBook_thenShouldThrowsNotificationException() {
        // given
        final var book = Book.with("title", "author", "isbn");

        final var expectedId = book.getId();
        final var expectedTitle = " ";
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        final var input = Input.from(expectedId, new UpdateBookRequest(expectedTitle, expectedAuthor, expectedIsbn));

        when(gateway.findById(expectedId)).thenReturn(Optional.of(Book.with(book)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(gateway).findById(expectedId);
        verify(gateway, never()).update(any());
    }

    @Test
    void givenAInvalidAuthor_whenCallsUpdateBook_thenShouldThrowsNotificationException() {
        // given
        final var book = Book.with("title", "author", "isbn");

        final var expectedId = book.getId();
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = " ";
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'author' should not be empty";

        final var input = Input.from(expectedId, new UpdateBookRequest(expectedTitle, expectedAuthor, expectedIsbn));

        when(gateway.findById(expectedId)).thenReturn(Optional.of(Book.with(book)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(gateway).findById(expectedId);
        verify(gateway, never()).update(any());
    }

    @Test
    void givenAInvalidIsbn_whenCallsUpdateBook_thenShouldThrowsNotificationException() {
        // given
        final var book = Book.with("title", "author", "isbn");

        final var expectedId = book.getId();
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = " ";

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'isbn' should not be empty";

        final var input = Input.from(expectedId, new UpdateBookRequest(expectedTitle, expectedAuthor, expectedIsbn));

        when(gateway.findById(expectedId)).thenReturn(Optional.of(Book.with(book)));

        // when
        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(gateway).findById(expectedId);
        verify(gateway, never()).update(any());
    }

    @Test
    void givenAValidId_whenCallsUpdateBookAndBookDoesNotExists_thenShouldThrowsNotFoundException() {
        // given
        final var expectedId = "123";
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorMessage = "Book with ID 123 was not found";

        final var input = Input.from(expectedId, new UpdateBookRequest(expectedTitle, expectedAuthor, expectedIsbn));

        when(gateway.findById(expectedId)).thenReturn(Optional.empty());

        // when
        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway).findById(expectedId);
        verify(gateway, never()).update(any());
    }
}