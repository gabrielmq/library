package io.github.gabrielmsouza.library.infrastructure.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "mail")
public class MailProperties {
    private String from;

    public MailProperties() {
    }

    public String from() {
        return from;
    }

    public void from(String from) {
        this.from = from;
    }
}
