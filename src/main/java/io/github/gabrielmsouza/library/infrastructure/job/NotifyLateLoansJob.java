package io.github.gabrielmsouza.library.infrastructure.job;

import io.github.gabrielmsouza.library.application.loan.NotifyLateLoansUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class NotifyLateLoansJob {
    private static final String CRON = "0 0 0 1/1 * ?";

    private final NotifyLateLoansUseCase notifyLateLoansUseCase;

    public NotifyLateLoansJob(final NotifyLateLoansUseCase notifyLateLoansUseCase) {
        this.notifyLateLoansUseCase = Objects.requireNonNull(notifyLateLoansUseCase);
    }

    @Scheduled(cron = CRON)
    public void send() {
        this.notifyLateLoansUseCase.execute();
    }
}
