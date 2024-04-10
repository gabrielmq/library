package io.github.gabrielmsouza.library.application;

public interface UnaryUseCase<IN> {
    void execute(IN in);
}
