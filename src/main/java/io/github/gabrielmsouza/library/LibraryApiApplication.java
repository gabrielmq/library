package io.github.gabrielmsouza.library;

import io.github.gabrielmsouza.library.infrastructure.configuration.WebServerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebServerConfiguration.class, args);
	}

}
