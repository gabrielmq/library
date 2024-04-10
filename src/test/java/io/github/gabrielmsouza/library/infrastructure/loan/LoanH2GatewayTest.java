package io.github.gabrielmsouza.library.infrastructure.loan;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.customer.Customer;
import io.github.gabrielmsouza.library.domain.loan.Loan;
import io.github.gabrielmsouza.library.domain.loan.LoanGateway;
import io.github.gabrielmsouza.library.domain.loan.LoanSearchQuery;
import io.github.gabrielmsouza.library.domain.pagination.SearchQuery;
import io.github.gabrielmsouza.library.infrastructure.H2GatewayTest;
import io.github.gabrielmsouza.library.infrastructure.book.BookH2Gateway;
import io.github.gabrielmsouza.library.infrastructure.customer.CustomerH2Gateway;
import io.github.gabrielmsouza.library.infrastructure.loan.persistence.LoanJpaEntity;
import io.github.gabrielmsouza.library.infrastructure.loan.persistence.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@H2GatewayTest
class LoanH2GatewayTest {
    @Autowired
    private LoanH2Gateway gateway;

    @Autowired
    private LoanRepository repository;

    @Autowired
    private BookH2Gateway bookGateway;

    @Autowired
    private CustomerH2Gateway customerGateway;

    @Test
    void givenAValidLoan_whenCallsCreate_thenShouldPersistIt() {
        // given
        final var customer = Customer.with(Fixture.Customer.name(), Fixture.Customer.email());
        this.customerGateway.create(customer);

        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());
        this.bookGateway.create(book);

        final var expectedBookId = book.getId();
        final var expectedCustomerId = customer.getId();

        final var loan = Loan.with(expectedCustomerId, expectedBookId);

        final var expectedId = loan.getId();

        assertEquals(0, this.repository.count());

        // when
        final var actualLoan = this.gateway.create(Loan.with(loan));

        // then
        assertEquals(1, this.repository.count());
        assertEquals(expectedId, actualLoan.getId());
        assertEquals(expectedBookId, actualLoan.getBookId());
        assertEquals(expectedCustomerId, actualLoan.getCustomerId());
        assertNotNull(actualLoan.getLoanDate());
        assertFalse(actualLoan.isReturned());

        final var persistedLoan = this.repository.findById(expectedId).get();
        assertEquals(expectedId, persistedLoan.getId());
        assertEquals(expectedBookId, persistedLoan.getBook().getId());
        assertEquals(expectedCustomerId, persistedLoan.getCustomer().getId());
        assertEquals(actualLoan.getLoanDate(), persistedLoan.getLoanDate());
        assertEquals(actualLoan.isReturned(), persistedLoan.isReturned());
    }

    @Test
    void givenAPrePersistedLoanedBookAndNotReturnedYet_whenCallsExistsByBookIdAndNotReturned_thenShouldReturnTrue() {
        // given
        final var customer = Customer.with(Fixture.Customer.name(), Fixture.Customer.email());
        this.customerGateway.create(customer);

        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());
        this.bookGateway.create(book);

        final var expectedBookId = book.getId();
        final var expectedCustomerId = customer.getId();

        final var loan = Loan.with(expectedCustomerId, expectedBookId);
        assertEquals(0, this.repository.count());

        this.gateway.create(Loan.with(loan));

        // when
        final var actualResult = this.gateway.existsByBookIdAndNotReturned(expectedBookId);

        // then
        assertEquals(1, this.repository.count());
        assertTrue(actualResult);
    }

    @Test
    void givenAPrePersistedLoanedBookAndReturned_whenCallsExistsByBookIdAndNotReturned_thenShouldReturnFalse() {
        // given
        final var customer = Customer.with(Fixture.Customer.name(), Fixture.Customer.email());
        this.customerGateway.create(customer);

        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());
        this.bookGateway.create(book);

        final var expectedBookId = book.getId();
        final var expectedCustomerId = customer.getId();

        final var loan = Loan.with(expectedCustomerId, expectedBookId);
        loan.returned();

        assertEquals(0, this.repository.count());

        this.gateway.create(Loan.with(loan));

        // when
        final var actualResult = this.gateway.existsByBookIdAndNotReturned(expectedBookId);

        // then
        assertEquals(1, this.repository.count());
        assertFalse(actualResult);
    }

    @Test
    void givenAValidId_whenCallsFindById_thenShouldReturnIt() {
        // given
        final var customer = Customer.with(Fixture.Customer.name(), Fixture.Customer.email());
        this.customerGateway.create(customer);

        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());
        this.bookGateway.create(book);

        final var expectedBookId = book.getId();
        final var expectedCustomerId = customer.getId();

        final var loan = Loan.with(expectedCustomerId, expectedBookId);
        final var expectedId = loan.getId();

        this.repository.saveAndFlush(LoanJpaEntity.from(loan));
        assertEquals(1, this.repository.count());

        // when
        final var actualLoan = this.gateway.findById(expectedId).get();

        // then
        assertEquals(expectedId, actualLoan.getId());
        assertEquals(expectedBookId, actualLoan.getBookId());
        assertEquals(expectedCustomerId, actualLoan.getCustomerId());
        assertNotNull(actualLoan.getLoanDate());
        assertFalse(actualLoan.isReturned());
    }

    @Test
    void givenAnInvalidId_whenCallsFindByIdAndDoesNotExists_thenShouldReturnEmpty() {
        // given
        final var customer = Customer.with(Fixture.Customer.name(), Fixture.Customer.email());
        this.customerGateway.create(customer);

        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());
        this.bookGateway.create(book);

        final var expectedBookId = book.getId();
        final var expectedCustomerId = customer.getId();

        final var loan = Loan.with(expectedCustomerId, expectedBookId);

        this.repository.saveAndFlush(LoanJpaEntity.from(loan));
        assertEquals(1, this.repository.count());

        // when
        final var actualLoan = this.gateway.findById("123");

        // then
        assertTrue(actualLoan.isEmpty());
    }

    @Test
    void givenAValidLoan_whenCallsUpdate_thenShouldRefreshIt() {
        // given
        final var customer = Customer.with(Fixture.Customer.name(), Fixture.Customer.email());
        this.customerGateway.create(customer);

        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());
        this.bookGateway.create(book);

        final var expectedBookId = book.getId();
        final var expectedCustomerId = customer.getId();

        final var loan = Loan.with(expectedCustomerId, expectedBookId);
        assertFalse(loan.isReturned());

        final var expectedId = loan.getId();

        this.repository.saveAndFlush(LoanJpaEntity.from(loan));
        assertEquals(1, this.repository.count());

        loan.returned();

        // when
        final var actualLoan = this.gateway.update(Loan.with(loan));

        // then
        assertEquals(1, this.repository.count());
        assertEquals(expectedId, actualLoan.getId());
        assertEquals(expectedBookId, actualLoan.getBookId());
        assertEquals(expectedCustomerId, actualLoan.getCustomerId());
        assertNotNull(actualLoan.getLoanDate());
        assertTrue(actualLoan.isReturned());

        final var persistedLoan = this.repository.findById(expectedId).get();
        assertEquals(expectedId, persistedLoan.getId());
        assertEquals(expectedBookId, persistedLoan.getBook().getId());
        assertEquals(expectedCustomerId, persistedLoan.getCustomer().getId());
        assertEquals(actualLoan.getLoanDate(), persistedLoan.getLoanDate());
        assertEquals(actualLoan.isReturned(), persistedLoan.isReturned());
    }

    @Test
    void givenAPrePersistedLoans_whenCallsFindAll_thenShouldReturnThem() {
        // given
        final var customer = Customer.with(Fixture.Customer.name(), Fixture.Customer.email());
        this.customerGateway.create(customer);

        final var books = List.of(
            Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn()),
            Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn())
        );

        books.forEach(this.bookGateway::create);

        final var expectedCustomerId = customer.getId();

        final var loans = books.stream().map(book -> Loan.with(expectedCustomerId, book.getId())).toList();
        loans.forEach(loan -> this.repository.saveAndFlush(LoanJpaEntity.from(loan)));
        assertEquals(2, this.repository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 2;

        final var query = new LoanSearchQuery(
                expectedPage,
                expectedPerPage,
                "",
                expectedCustomerId,
                "loanDate",
                "asc"
        );

        // when
        final var actualResult = this.gateway.findAll(query);

        // then
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
    }

    @Test
    void givenAllParams_whenCallsFindAll_thenShouldReturnFilteredList() {
        // given
        final var customer = Customer.with(Fixture.Customer.name(), Fixture.Customer.email());
        this.customerGateway.create(customer);

        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());
        this.bookGateway.create(book);

        final var expectedCustomerId = customer.getId();

        final var loan = Loan.with(expectedCustomerId, book.getId());
        this.repository.saveAndFlush(LoanJpaEntity.from(loan));
        assertEquals(1, this.repository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var query = new LoanSearchQuery(
                expectedPage,
                expectedPerPage,
                book.getIsbn(),
                expectedCustomerId,
                "loanDate",
                "asc"
        );

        // when
        final var actualResult = this.gateway.findAll(query);

        // then
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
    }

    @Test
    void givenAEmptyLoanTable_whenCallsFindAll_thenShouldReturnEmptyPage() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        assertEquals(0, this.repository.count());

        final var query = new LoanSearchQuery(
                expectedPage,
                expectedPerPage,
                "123",
                "456",
                "loanDate",
                "asc"
        );
        // when
        final var actualResult = this.gateway.findAll(query);

        // then
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(0, actualResult.items().size());
    }
}