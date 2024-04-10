package io.github.gabrielmsouza.library.infrastructure.book.persistnce;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<BookJpaEntity, String> {
    boolean existsByIsbn(String isbn);
    Page<BookJpaEntity> findAll(Specification<BookJpaEntity> whereClause, Pageable page);
    Optional<BookJpaEntity> findByIsbn(String isbn);

}
