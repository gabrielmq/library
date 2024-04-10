package io.github.gabrielmsouza.library.infrastructure.loan.persistence;

import io.github.gabrielmsouza.library.domain.customer.Customer;
import io.github.gabrielmsouza.library.domain.loan.Loan;
import io.github.gabrielmsouza.library.infrastructure.book.persistnce.BookJpaEntity;
import io.github.gabrielmsouza.library.infrastructure.customer.persistence.CustomerJpaEntity;
import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "Loans")
@Table(name = "loans")
public class LoanJpaEntity {
    @Id
    @Column
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerJpaEntity customer;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookJpaEntity book;

    @Column(name = "loan_date", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant loanDate;

    @Column(name = "return_date", columnDefinition = "DATETIME(6)")
    private Instant returnDate;

    @Column(name = "returned", nullable = false)
    private boolean returned;

    @Deprecated
    LoanJpaEntity() {}

    private LoanJpaEntity(final String id) {
        this.id = id;
    }

    private LoanJpaEntity(
        final String id,
        final CustomerJpaEntity customer,
        final BookJpaEntity book,
        final Instant loanDate,
        final Instant returnDate,
        final boolean returned
    ) {
        this.id = id;
        this.customer = customer;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.returned = returned;
        this.book = book;
    }

    public static LoanJpaEntity from(final String id) {
        return new LoanJpaEntity(id);
    }

    public static LoanJpaEntity from(final Loan loan) {
        return new LoanJpaEntity(
            loan.getId(),
            CustomerJpaEntity.from(loan.getCustomerId()),
            BookJpaEntity.from(loan.getBookId()),
            loan.getLoanDate(),
            loan.getReturnDate(),
            loan.isReturned()
        );
    }

    public Loan toLoan() {
        return Loan.with(
                this.id,
                this.customer.getId(),
                this.book.getId(),
                this.loanDate,
                this.returnDate,
                this.returned
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CustomerJpaEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerJpaEntity customer) {
        this.customer = customer;
    }

    public BookJpaEntity getBook() {
        return book;
    }

    public void setBook(BookJpaEntity book) {
        this.book = book;
    }

    public Instant getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Instant loanDate) {
        this.loanDate = loanDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public Instant getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Instant returnDate) {
        this.returnDate = returnDate;
    }
}
