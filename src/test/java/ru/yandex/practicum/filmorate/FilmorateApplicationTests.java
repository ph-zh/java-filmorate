package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {
    FilmStorage filmStorage;
    UserStorage userStorage;
    FilmService filmService;
    UserService userService;
    FilmController filmController;
    UserController userController;
    Film film;
    User user;

    @BeforeEach
    void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
        userService = new UserService(userStorage);
        filmController = new FilmController(filmService);
        userController = new UserController(userService);
        film = new Film(0, "Film", "film description", LocalDate.now(), 120);
        user = new User(0, "user@email.ru", "login", "name",
                LocalDate.of(2000, Month.DECEMBER, 20));
    }

    @Test
    void contextLoads() {
    }

    void addFilmAndUserToStorages() {
        filmController.create(film);
        userController.create(user);
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

        FilmNotFoundException exception = assertThrows(
                FilmNotFoundException.class, () -> {
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
        assertEquals("Пользователь с id: 1 уже существует", exception.getMessage());
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

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class, () -> {
                    userController.update(user);
                }
        );
        assertEquals("Пользователя с id: 10 не существует", exception.getMessage());
    }

    @Test
    void findAllFilm() {
        Film film2 = new Film(0, "Film2", "film2 description", LocalDate.now(), 90);

        filmController.create(film2);
        filmController.create(film);

        assertEquals(filmController.findAll().size(), 2);
    }

    @Test
    void findAllUser() {
        addFilmAndUserToStorages();

        User user2 = new User(0, "user2@email.ru", "login2", "name2",
                LocalDate.of(2000, Month.DECEMBER, 30));
        userController.create(user2);

        assertEquals(userController.findAll().size(), 2);
    }

    @Test
    void getFilmById() {
        filmController.create(film);
        Film testFilm = filmController.getById(1);

        film.setId(1);

        assertEquals(testFilm, film);
    }

    @Test
    void getUserById() {
        userController.create(user);
        User testUser = userController.getById(1);

        user.setId(1);

        assertEquals(testUser, user);
    }

    @Test
    void getFilmByIdWithNotExistingId() {
        FilmNotFoundException exception = assertThrows(
                FilmNotFoundException.class, () -> {
                    filmController.getById(10);
                }
        );
        assertEquals("Фильма с id: 10 не существует, получение невозможно", exception.getMessage());
    }

    @Test
    void getUserByIdWithNotExistingId() {
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class, () -> {
                    userController.getById(10);
                }
        );
        assertEquals("Пользователя с id: 10 не существует", exception.getMessage());
    }

    @Test
    void addLike() {
        addFilmAndUserToStorages();

        filmController.addLike(1, 1);

        assertTrue(film.getLikesByUsers().contains(1));
    }

    @Test
    void removeLike() {
        addFilmAndUserToStorages();

        filmController.addLike(1, 1);
        filmController.removeLike(1, 1);

        assertTrue(film.getLikesByUsers().isEmpty());
    }

    @Test
    void getPopular() {
        addFilmAndUserToStorages();
        Film film2 = new Film(0, "Film2", "film2 description", LocalDate.now(), 90);
        User user2 = new User(0, "user2@email.ru", "login2", "name2",
                LocalDate.of(2000, Month.DECEMBER, 30));

        filmController.create(film2);
        userController.create(user2);

        filmController.addLike(2, 1);
        filmController.addLike(2, 2);

        List<Film> list = new LinkedList<>(List.of(film2, film));

        assertEquals(filmController.getPopular(10), list);
    }

    @Test
    void addFriend() {
        addFilmAndUserToStorages();

        User user2 = new User(0, "user2@email.ru", "login2", "name2",
                LocalDate.of(2000, Month.DECEMBER, 30));
        userController.create(user2);

        userController.addFriend(1, 2);

        assertTrue(user.getFriends().contains(2));
        assertTrue(user2.getFriends().contains(1));
    }

    @Test
    void removeFriend() {
        addFilmAndUserToStorages();

        User user2 = new User(0, "user2@email.ru", "login2", "name2",
                LocalDate.of(2000, Month.DECEMBER, 30));
        userController.create(user2);

        userController.addFriend(1, 2);
        userController.removeFriend(1, 2);

        assertTrue(user.getFriends().isEmpty());
        assertTrue(user2.getFriends().isEmpty());
    }

    @Test
    void getFriends() {
        addFilmAndUserToStorages();

        User user2 = new User(0, "user2@email.ru", "login2", "name2",
                LocalDate.of(2000, Month.DECEMBER, 30));
        User user3 = new User(0, "user3@email.ru", "login3", "name3",
                LocalDate.of(2000, Month.DECEMBER, 31));
        userController.create(user2);
        userController.create(user3);

        userController.addFriend(1, 2);
        userController.addFriend(1, 3);

        assertEquals(userController.getFriends(1), List.of(user2, user3));
    }

    @Test
    void getCommonFriends() {
        addFilmAndUserToStorages();

        User user2 = new User(0, "user2@email.ru", "login2", "name2",
                LocalDate.of(2000, Month.DECEMBER, 30));
        User user3 = new User(0, "user3@email.ru", "login3", "name3",
                LocalDate.of(2000, Month.DECEMBER, 31));
        userController.create(user2);
        userController.create(user3);

        userController.addFriend(1, 2);
        userController.addFriend(1, 3);

        assertEquals(userController.getCommonFriends(2, 3), List.of(user));
    }
}