package io.github.gabrielmsouza.library.application.book;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.UseCaseTest;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.exceptions.InternalErrorException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeleteBookUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteBookUseCase useCase;

    @Mock
    private BookGateway gateway;

    @Test
    void givenAValidId_whenCallsDeleteBookById_thenShouldDeleteIt() {
        // given
        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());
        final var expectedId = book.getId();

        doNothing().when(this.gateway).deleteById(expectedId);

        // when
        assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        // then
        verify(this.gateway).deleteById(expectedId);
    }

    @Test
    void givenAValidId_whenCallsDeleteBookByIdAndBookDoesNotExists_thenShouldBeOk() {
        // given
        final var expectedId = Fixture.randomId();

        doNothing().when(this.gateway).deleteById(any());

        // when
        assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        // then
        verify(this.gateway).deleteById(expectedId);
    }

    @Test
    void givenAValidId_whenCallsDeleteBookByIdAndGatewayThrowsUnexpectedError_thenShouldReceiveException() {
        // given
        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());
        final var expectedId = book.getId();

        doThrow(InternalErrorException.with("Gateway error")).when(this.gateway).deleteById(any());

        // when
        assertThrows(InternalErrorException.class, () -> this.useCase.execute(expectedId));

        // then
        verify(this.gateway).deleteById(expectedId);
    }
}