package io.github.gabrielmsouza.library.application.book;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.UseCaseTest;
import io.github.gabrielmsouza.library.application.book.GetBookByIdUseCase;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetBookByIdUseCaseTest extends UseCaseTest {
    @InjectMocks
    private GetBookByIdUseCase useCase;

    @Mock
    private BookGateway gateway;

    @Test
    void givenAValidId_whenCallsGetBookById_thenShouldReturnIt() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var book = Book.with(expectedTitle, expectedAuthor, expectedIsbn);

        final var expectedId = book.getId();

        when(this.gateway.findById(expectedId)).thenReturn(Optional.of(book));

        // when
        final var actualBook = this.useCase.execute(expectedId);

        // then
        assertNotNull(actualBook);
        assertEquals(expectedId, actualBook.id());
        assertEquals(expectedTitle, actualBook.title());
        assertEquals(expectedAuthor, actualBook.author());
        assertEquals(expectedIsbn, actualBook.isbn());

        verify(this.gateway).findById(expectedId);
    }

    @Test
    void givenAnInvalidId_whenCallsGetBookByIdAndDoesNotExists_thenShouldThrowNotFoundException() {
        // given
        final var expectedId = "123";
        final var expectedErrorMessage = "Book with ID 123 was not found";

        when(this.gateway.findById(expectedId)).thenReturn(Optional.empty());

        // when
        final var actualException = assertThrows(NotFoundException.class, () -> this.useCase.execute(expectedId));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(this.gateway).findById(expectedId);
    }
}