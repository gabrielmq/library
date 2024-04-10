package io.github.gabrielmsouza.library.domain.book;

import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.domain.validation.Error;
import io.github.gabrielmsouza.library.domain.validation.ValidationHandler;
import io.github.gabrielmsouza.library.domain.validation.handler.Notification;

import java.util.*;

public class Book {
    private final String id;
    private String title;
    private String author;
    private String isbn;
    private final List<String> loans;

    private Book(final String id, final String title, final String author, final String isbn, final List<String> loans) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.loans = new ArrayList<>(loans);
        selfValidation();
    }

    public static Book with(final String title, final String author, final String isbn) {
        final var id = UUID.randomUUID().toString().toLowerCase().replace("-", "");
        return new Book(id, title, author, isbn, new ArrayList<>());
    }

    public static Book with(final String id, final String title, final String author, final String isbn, final List<String> loans) {
        return new Book(id, title, author, isbn, loans);
    }

    public static Book with(final Book book) {
        return new Book(book.id, book.title, book.author, book.isbn, book.loans);
    }

    public Book update(final String title, final String author, final String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        selfValidation();
        return this;
    }

    public void validate(final ValidationHandler handler) {
        if (Objects.isNull(this.id) || this.id.isBlank()) {
            handler.append(Error.with("'id' should not be empty"));
        }

        if (Objects.isNull(this.title) || this.title.isBlank()) {
            handler.append(Error.with("'title' should not be empty"));
        }

        if (Objects.isNull(this.author) || this.author.isBlank()) {
            handler.append(Error.with("'author' should not be empty"));
        }

        if (Objects.isNull(this.isbn) || this.isbn.isBlank()) {
            handler.append(Error.with("'isbn' should not be empty"));
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public List<String> getLoans() {
        return Collections.unmodifiableList(loans);
    }

    public void addLoan(final String loanId) {
        this.loans.add(loanId);
    }

    private void selfValidation() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasErrors()) {
            throw NotificationException.with("Failed to create a Book", notification);
        }
    }
}
