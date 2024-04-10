package io.github.gabrielmsouza.library.infrastructure.loan.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<LoanJpaEntity, String> {
    @Query("""
            SELECT
                CASE WHEN ( COUNT(l.id) > 0 ) THEN TRUE ELSE FALSE END
            FROM
                Loans l
            WHERE
                l.book.id = :bookId AND ( l.returned IS NULL OR l.returned IS NOT TRUE )
    """)
    boolean existsByBookIdAndReturned(@Param("bookId") String bookId);

    @Query("""
        SELECT
            l
        FROM
            Loans l
        JOIN
            l.book as b
        WHERE b.isbn = :isbn or l.customer.id = :customerId
    """)
    Page<LoanJpaEntity> findAll(
            @Param("isbn") String isbn,
            @Param("customerId") String customerId,
            Pageable pageable
    );

    @Query("""
        SELECT
            l
        FROM
            Loans l
        WHERE
            l.loanDate <= :days AND ( l.returned IS NULL OR l.returned IS FALSE )
    """)
    List<LoanJpaEntity> findAllLateLoans(@Param("days") LocalDate days);
}
