package io.github.gabrielmsouza.library.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration(proxyBeanMethods = false)
@ComponentScan("io.github.gabrielmsouza.library")
public class WebServerConfiguration {
}
