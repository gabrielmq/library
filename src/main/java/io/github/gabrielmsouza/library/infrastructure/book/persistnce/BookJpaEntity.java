package io.github.gabrielmsouza.library.infrastructure.book.persistnce;

import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.infrastructure.loan.persistence.LoanJpaEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Books")
@Table(name = "books")
public class BookJpaEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<LoanJpaEntity> loans;

    @Deprecated
    BookJpaEntity() {}

    private BookJpaEntity(final String id) {
        this.id = id;
    }

    private BookJpaEntity(final String id, final String title, final String author, final String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.loans = new ArrayList<>();
    }

    public static BookJpaEntity from(final Book book) {
        final var bookEntity = new BookJpaEntity(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn()
        );
        book.getLoans().forEach(bookEntity::addLoan);
        return bookEntity;
    }

    public static BookJpaEntity from(final String id) {
        return new BookJpaEntity(id);
    }

    public Book toBook() {
        final var loans = this.loans.stream()
                .map(LoanJpaEntity::getId)
                .toList();
        return Book.with(this.id, this.title, this.author, this.isbn, loans);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<LoanJpaEntity> getLoans() {
        return loans;
    }

    public void setLoans(List<LoanJpaEntity> loans) {
        this.loans = loans;
    }

    private void addLoan(final String loanId) {
        this.loans.add(LoanJpaEntity.from(loanId));
    }
}
