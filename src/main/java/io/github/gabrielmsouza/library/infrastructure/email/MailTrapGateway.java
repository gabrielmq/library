package io.github.gabrielmsouza.library.infrastructure.email;

import io.github.gabrielmsouza.library.domain.exceptions.InternalErrorException;
import io.github.gabrielmsouza.library.domain.mail.EmailGateway;
import io.github.gabrielmsouza.library.infrastructure.configuration.properties.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MailTrapGateway implements EmailGateway {
    private static final String SUBJECT = "Book with overdue loan";
    private final JavaMailSender mailSender;
    private final String from;

    public MailTrapGateway(
            final JavaMailSender mailSender,
            final MailProperties props
    ) {
        this.mailSender = Objects.requireNonNull(mailSender);
        this.from = Objects.requireNonNull(props).from();
    }

    public void send(final String email) {
        try {
            final var message = new SimpleMailMessage();
            message.setFrom(from);
            message.setSubject(SUBJECT);
            message.setText("You have a book with overdue loan.");
            message.setTo(email);
            this.mailSender.send(message);
        } catch (Throwable e) {
            throw InternalErrorException.with("Error observed from email sender", e);
        }
    }
}
