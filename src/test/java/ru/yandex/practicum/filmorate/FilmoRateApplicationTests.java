package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmoRateApplicationTests {
    private final UsersDao userStorage;
    private final FilmsDao filmStorage;
    private final FriendsDao friendsDao;
    private final GenreDao genreDao;
    private final LikesDao likesDao;
    private final MPADao mpaDao;
    private final JdbcTemplate jdbcTemplate;
    private User user;
    private Film film;

    @BeforeEach
    public void addDataAndRestartDb() {
        jdbcTemplate.update("DELETE FROM likes_by_users");
        jdbcTemplate.update("DELETE FROM film_genres");
        jdbcTemplate.update("DELETE FROM friends");
        jdbcTemplate.update("DELETE FROM films");
        jdbcTemplate.update("DELETE FROM users");

        jdbcTemplate.update("ALTER TABLE films ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");

        user = User.builder()
                .email("email@email.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 10))
                .build();

        userStorage.create(user);

        film = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2010, Month.DECEMBER, 10))
                .duration(100)
                .mpa(new MPA(1, null, null))
                .genres(List.of(new Genre(1, null)))
                .build();

        filmStorage.create(film);
    }

    @Test
    public void testFindUserById() {
        User testUser = userStorage.getById(1);

        assertThat(testUser).hasFieldOrPropertyWithValue("id", 1);

        assertEquals(user, testUser);
    }

    @Test
    public void testFindUserWithWrongId() {
        UserNotFoundException e = assertThrows(UserNotFoundException.class, () -> {
            userStorage.getById(100);
        });

        assertEquals(e.getMessage(), "User with id: 100 not found in DB");
    }

    @Test
    public void testFindAllUsers() {
        Collection<User> users = userStorage.findAll();

        assertEquals(1, users.size());
        assertTrue(users.contains(user));
    }

    @Test
    public void testCreateUser() {
        User userTest = User.builder()
                .email("email2@email.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 20))
                .build();

        userStorage.create(userTest);

        assertThat(userTest).hasFieldOrPropertyWithValue("id", 2);

        assertEquals(userTest, userStorage.getById(2));
    }

    @Test
    public void testCreateUserWithSomeId() {
        User userTest = User.builder()
                .id(100)
                .email("email2@email.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 20))
                .build();

        userStorage.create(userTest);

        assertThat(userTest).hasFieldOrPropertyWithValue("id", 2);

        assertEquals(userTest, userStorage.getById(2));
    }

    @Test
    public void testUpdateUser() {
        User userTest = User.builder()
                .id(1)
                .email("email2@email.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 20))
                .build();

        userStorage.update(userTest);

        assertThat(userTest).hasFieldOrPropertyWithValue("id", 1);

        assertEquals(userTest, userStorage.getById(1));
    }

    @Test
    public void testUpdateUserWithWrongId() {
        User userTest = User.builder()
                .id(100)
                .email("email2@email.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 20))
                .build();

        UserNotFoundException e = assertThrows(UserNotFoundException.class, () -> {
            userStorage.update(userTest);
        });

        assertEquals(e.getMessage(), "User with id: 100 not found in DB");
    }

    @Test
    public void testFindFilmById() {
        Film testFilm = filmStorage.getById(1);

        assertThat(testFilm).hasFieldOrPropertyWithValue("id", 1);

        assertEquals(film, testFilm);
    }

    @Test
    public void testFindFilmWithWrongId() {
        FilmNotFoundException e = assertThrows(FilmNotFoundException.class, () -> {
            filmStorage.getById(100);
        });

        assertEquals(e.getMessage(), "Film with id: 100 not found");
    }

    @Test
    public void testFindAllFilms() {
        Collection<Film> films = filmStorage.findAll();

        assertEquals(1, films.size());
        assertTrue(films.contains(film));
    }

    @Test
    public void testCreateFilm() {
        Film testFilm = Film.builder()
                .name("film2")
                .description("description2")
                .releaseDate(LocalDate.of(2010, Month.DECEMBER, 20))
                .duration(200)
                .mpa(new MPA(1, null, null))
                .genres(List.of(new Genre(1, null)))
                .build();

        filmStorage.create(testFilm);

        assertThat(testFilm).hasFieldOrPropertyWithValue("id", 2);

        assertEquals(testFilm, filmStorage.getById(2));
    }

    @Test
    public void testCreateFilmWithSomeId() {
        Film testFilm = Film.builder()
                .id(100)
                .name("film2")
                .description("description2")
                .releaseDate(LocalDate.of(2010, Month.DECEMBER, 20))
                .duration(200)
                .mpa(new MPA(1, null, null))
                .genres(List.of(new Genre(1, null)))
                .build();

        filmStorage.create(testFilm);

        assertThat(testFilm).hasFieldOrPropertyWithValue("id", 2);

        assertEquals(testFilm, filmStorage.getById(2));
    }

    @Test
    public void testUpdateFilm() {
        Film testFilm = Film.builder()
                .id(1)
                .name("film2")
                .description("description2")
                .releaseDate(LocalDate.of(2010, Month.DECEMBER, 20))
                .duration(200)
                .mpa(new MPA(1, null, null))
                .genres(List.of(new Genre(1, null)))
                .build();

        filmStorage.update(testFilm);

        assertThat(testFilm).hasFieldOrPropertyWithValue("id", 1);

        assertEquals(testFilm, filmStorage.getById(1));
    }

    @Test
    public void testUpdateFilmWithWrongId() {
        Film testFilm = Film.builder()
                .id(100)
                .name("film2")
                .description("description2")
                .releaseDate(LocalDate.of(2010, Month.DECEMBER, 20))
                .duration(200)
                .mpa(new MPA(2, "mpa2", "mpa-desc2"))
                .genres(List.of(new Genre(2, "genre2")))
                .build();

        FilmNotFoundException e = assertThrows(FilmNotFoundException.class, () -> {
            filmStorage.update(testFilm);
        });

        assertEquals(e.getMessage(), "Film with id: 100 not found");
    }

    @Test
    public void testAddFriend() {
        User userTest = User.builder()
                .email("email2@email.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 20))
                .build();

        userStorage.create(userTest);

        friendsDao.addFriend(user.getId(), userTest.getId());

        assertTrue(friendsDao.getFriends(user.getId()).contains(userTest));
    }

    @Test
    public void testAddFriendWithWrongId() {
        UserNotFoundException e = assertThrows(UserNotFoundException.class, () -> {
            friendsDao.addFriend(1, 100);
        });

        assertEquals(e.getMessage(), "User with id: 100 not found in DB");
    }

    @Test
    public void testRemoveFriend() {
        User userTest = User.builder()
                .email("email2@email.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 20))
                .build();

        userStorage.create(userTest);

        friendsDao.addFriend(user.getId(), userTest.getId());

        friendsDao.removeFriend(user.getId(), userTest.getId());

        assertFalse(friendsDao.getFriends(user.getId()).contains(userTest));
    }

    @Test
    public void testGetFriends() {
        User userTest = User.builder()
                .email("email2@email.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 20))
                .build();

        userStorage.create(userTest);

        friendsDao.addFriend(user.getId(), userTest.getId());

        assertEquals(friendsDao.getFriends(user.getId()), List.of(userTest));
    }

    @Test
    public void testGetCommonFriends() {
        User userTest = User.builder()
                .email("email2@email.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 20))
                .build();

        userStorage.create(userTest);

        friendsDao.addFriend(user.getId(), userTest.getId());

        User userTest2 = User.builder()
                .email("email3@email.ru")
                .login("login3")
                .name("name3")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 20))
                .build();

        userStorage.create(userTest2);

        friendsDao.addFriend(userTest2.getId(), userTest.getId());

        assertEquals(friendsDao.getCommonFriends(user.getId(), userTest2.getId()), List.of(userTest));
    }

    @Test
    public void testGetGenreById() {
        Genre genre = genreDao.getGenreById(1);

        assertThat(genre).hasFieldOrPropertyWithValue("id", 1);
        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void testGetGenreByIdWithWrongId() {
        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> {
            genreDao.getGenreById(10);
        });

        assertEquals(e.getMessage(), "Genre with id: 10 not found in DB");
    }

    @Test
    public void testGetAllGenres() {
        assertEquals(genreDao.findAll().size(), 6);
    }

    @Test
    public void testAddLike() {
        likesDao.addLike(film.getId(), user.getId());

        Boolean bool = jdbcTemplate.queryForObject("select exists " +
                "(select * from likes_by_users where id_film = ? and id_user = ?)",
                Boolean.class, film.getId(), user.getId());

        assertTrue(bool);
    }

    @Test
    public void testRemoveLike() {
        likesDao.addLike(film.getId(), user.getId());
        likesDao.removeLike(film.getId(), user.getId());

        Boolean bool = jdbcTemplate.queryForObject("select exists " +
                        "(select * from likes_by_users where id_film = ? and id_user = ?)",
                Boolean.class, film.getId(), user.getId());

        assertFalse(bool);
    }

    @Test
    public void testGetPopular() {
        Film testFilm = Film.builder()
                .name("film2")
                .description("description2")
                .releaseDate(LocalDate.of(2010, Month.DECEMBER, 20))
                .duration(200)
                .mpa(new MPA(1, null, null))
                .genres(List.of(new Genre(1, null)))
                .build();

        filmStorage.create(testFilm);

        Film testFilm2 = Film.builder()
                .name("film3")
                .description("description3")
                .releaseDate(LocalDate.of(2010, Month.DECEMBER, 20))
                .duration(300)
                .mpa(new MPA(2, null, null))
                .genres(List.of(new Genre(2, null)))
                .build();

        filmStorage.create(testFilm2);

        User userTest = User.builder()
                .email("email2@email.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 20))
                .build();

        userStorage.create(userTest);

        likesDao.addLike(film.getId(), user.getId());
        likesDao.addLike(film.getId(), userTest.getId());

        likesDao.addLike(testFilm.getId(), user.getId());

        assertEquals(likesDao.getPopular(10), List.of(film, testFilm, testFilm2));
    }

    @Test
    public void testGetMpaById() {
        MPA mpa = mpaDao.getMpaById(1);

        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1);
        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void testGetMpaByIdWithWrongId() {
        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class, () -> {
            mpaDao.getMpaById(10);
        });

        assertEquals(e.getMessage(), "Rating MPA with id: 10 not found in DB");
    }

    @Test
    public void testGetAllMpa() {
        assertEquals(mpaDao.findAll().size(), 5);
    }

}
