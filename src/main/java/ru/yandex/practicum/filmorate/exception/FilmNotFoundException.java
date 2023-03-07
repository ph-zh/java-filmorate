package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException() {
    }

    public FilmNotFoundException(String message) {
        super(message);
    }

    public FilmNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilmNotFoundException(Throwable cause) {
        super(cause);
    }

    public FilmNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}