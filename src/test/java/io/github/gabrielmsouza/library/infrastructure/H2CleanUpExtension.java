package io.github.gabrielmsouza.library.infrastructure;

import io.github.gabrielmsouza.library.infrastructure.book.persistnce.BookRepository;
import io.github.gabrielmsouza.library.infrastructure.customer.persistence.CustomerRepository;
import io.github.gabrielmsouza.library.infrastructure.loan.persistence.LoanRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class H2CleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);
        cleanUp(List.of(
                appContext.getBean(BookRepository.class),
                appContext.getBean(LoanRepository.class),
                appContext.getBean(CustomerRepository.class)
        ));
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
