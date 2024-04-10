package io.github.gabrielmsouza.library.infrastructure.loan;

import io.github.gabrielmsouza.library.domain.loan.Loan;
import io.github.gabrielmsouza.library.domain.loan.LoanGateway;
import io.github.gabrielmsouza.library.domain.loan.LoanSearchQuery;
import io.github.gabrielmsouza.library.domain.pagination.Pagination;
import io.github.gabrielmsouza.library.infrastructure.loan.persistence.LoanJpaEntity;
import io.github.gabrielmsouza.library.infrastructure.loan.persistence.LoanRepository;
import io.github.gabrielmsouza.library.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class LoanH2Gateway implements LoanGateway {
    private final LoanRepository repository;

    public LoanH2Gateway(final LoanRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Loan create(final Loan loan) {
        return save(loan);
    }

    @Override
    public Loan update(final Loan loan) {
        return save(loan);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Loan> findById(final String id) {
        return this.repository.findById(id).map(LoanJpaEntity::toLoan);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByBookIdAndNotReturned(final String bookId) {
        return this.repository.existsByBookIdAndReturned(bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<Loan> findAll(final LoanSearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var result = this.repository.findAll(query.isbn(), query.customerId(), page);
        return new Pagination<>(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getContent().stream().map(LoanJpaEntity::toLoan).toList()
        );
    }

    @Override
    public List<Loan> findAllLateLoans(final LocalDate date) {
        return this.repository.findAllLateLoans(date).stream()
                .map(LoanJpaEntity::toLoan)
                .toList();
    }

    private Loan save(final Loan loan) {
        return this.repository.save(LoanJpaEntity.from(loan)).toLoan();
    }
}
