package io.github.gabrielmsouza.library.application;

public interface UseCase<IN, OUT> {
    OUT execute(IN in);
}
