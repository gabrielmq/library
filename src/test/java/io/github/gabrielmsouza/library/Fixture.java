package io.github.gabrielmsouza.library;

import net.datafaker.Faker;

public final class Fixture {
    private static final Faker FAKER = new Faker();

    private Fixture() {}

    public static String randomId() {
        return FAKER.internet().uuid().toLowerCase().replace("-", "");
    }

    public static class Book {
        public static String title() {
            return FAKER.book().title();
        }

        public static String author() {
            return FAKER.book().author();
        }

        public static String isbn() {
            return FAKER.code().isbn10();
        }
    }

    public static class Customer {
        public static String name() {
            return FAKER.name().fullName();
        }

        public static String email() {
            return FAKER.internet().emailAddress();
        }
    }
}
