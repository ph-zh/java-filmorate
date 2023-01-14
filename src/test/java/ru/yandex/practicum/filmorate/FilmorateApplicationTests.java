package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

    FilmController filmController;
    UserController userController;
    Film film;
    User user;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        userController = new UserController();
        film = new Film(0, "Film", "film description", LocalDate.now(), 120);
        user = new User(0, "user@email.ru", "login", "name",
                LocalDate.of(2000, Month.DECEMBER, 20));
    }

    @Test
    void contextLoads() {
    }

    @Test
    void addFilmWithCorrectFields() {
        filmController.create(film);

        assertEquals(film.getId(), 1);
    }

    @Test
    void addFilmWithExistingId() {
        filmController.create(film);

        Film testFilm = new Film(1, "testFilm", "testFilm description", LocalDate.now(), 120);

        ValidationException exception = assertThrows(
                ValidationException.class, () -> {
                    filmController.create(testFilm);
                }
        );
        assertEquals("Фильм с таким id уже существует", exception.getMessage());
    }

    @Test
    void addFilmWithWrongName() {
        film.setName("");
        ValidationException exception = assertThrows(
                ValidationException.class, () -> {
                    filmController.create(film);
                }
        );
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void addFilmWithWrongDescription() {
        film.setDescription("description description description description description description description " +
                "description description description description description description description description " +
                "description description description description description description description description");
        ValidationException exception = assertThrows(
                ValidationException.class, () -> {
                    filmController.create(film);
                }
        );
        assertEquals("Описание фильма превышает максимальное количество знаков 200", exception.getMessage());
    }

    @Test
    void addFilmWithWrongReleaseDate() {
        film.setReleaseDate(LocalDate.of(1800, Month.DECEMBER, 1));
        ValidationException exception = assertThrows(
                ValidationException.class, () -> {
                    filmController.create(film);
                }
        );
        assertEquals("Дата релиза фильма введена неверна", exception.getMessage());
    }

    @Test
    void addFilmWithWrongDuration() {
        film.setDuration(-100);
        ValidationException exception = assertThrows(
                ValidationException.class, () -> {
                    filmController.create(film);
                }
        );
        assertEquals("Продолжительность фильма не может быть отрицательной", exception.getMessage());
    }

    @Test
    void updateFilmWithExistingId() {
        filmController.create(film);
        film.setName("New film name");

        filmController.update(film);
    }

    @Test
    void updateFilmWithNotExistingId() {
        filmController.create(film);
        film.setId(10);

        ValidationException exception = assertThrows(
                ValidationException.class, () -> {
                    filmController.update(film);
                }
        );
        assertEquals("Фильма с таким id не существует, обновление невозможно", exception.getMessage());
    }

    @Test
    void addUserWithCorrectFields() {
        userController.create(user);

        assertEquals(user.getId(), 1);
    }

    @Test
    void addUserWithExistingId() {
        userController.create(user);

        User testUser = new User(1, "user@email.ru", "login", "name",
                LocalDate.of(2000, Month.DECEMBER, 20));

        ValidationException exception = assertThrows(
                ValidationException.class, () -> {
                    userController.create(testUser);
                }
        );
        assertEquals("Пользователь с таким id уже существует", exception.getMessage());
    }

    @Test
    void addUserWithWrongEmail() {
        user.setEmail("email");
        ValidationException exception = assertThrows(
                ValidationException.class, () -> {
                    userController.create(user);
                }
        );
        assertEquals("Адрес почты введён неверно", exception.getMessage());
    }

    @Test
    void addUserWithWrongLogin() {
        user.setLogin("log in");
        ValidationException exception = assertThrows(
                ValidationException.class, () -> {
                    userController.create(user);
                }
        );
        assertEquals("Логин не должен быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void addUserWithWrongReleaseDate() {
        user.setBirthday(LocalDate.of(2130, Month.DECEMBER, 1));
        ValidationException exception = assertThrows(
                ValidationException.class, () -> {
                    userController.create(user);
                }
        );
        assertEquals("День рождения не может быть из будущего :)", exception.getMessage());
    }

    @Test
    void addUserWithEmptyName() {
        user.setName("");
        userController.create(user);

        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void updateUserWithExistingId() {
        userController.create(user);
        user.setLogin("login_new");

        userController.update(user);
    }

    @Test
    void updateUserWithNotExistingId() {
        userController.create(user);
        user.setId(10);

        ValidationException exception = assertThrows(
                ValidationException.class, () -> {
                    userController.update(user);
                }
        );
        assertEquals("Пользователь с таким id ещё не создан", exception.getMessage());
    }
}
