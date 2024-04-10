package io.github.gabrielmsouza.library.infrastructure.configuration.usecases;

import io.github.gabrielmsouza.library.application.book.*;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(proxyBeanMethods = false)
public class BookUseCaseConfiguration {
    private final BookGateway bookGateway;

    public BookUseCaseConfiguration(final BookGateway bookGateway) {
        this.bookGateway = Objects.requireNonNull(bookGateway);
    }

    @Bean
    CreateBookUseCase createBookUseCase() {
        return new CreateBookUseCase(bookGateway);
    }

    @Bean
    UpdateBookUseCase updateBookUseCase() {
        return new UpdateBookUseCase(bookGateway);
    }

    @Bean
    DeleteBookUseCase deleteBookUseCase() {
        return new DeleteBookUseCase(bookGateway);
    }

    @Bean
    GetBookByIdUseCase getBookByIdUseCase() {
        return new GetBookByIdUseCase(bookGateway);
    }

    @Bean
    ListBooksUseCase listBooksUseCase() {
        return new ListBooksUseCase(bookGateway);
    }
}
