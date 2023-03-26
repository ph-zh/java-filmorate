package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.FriendsDao;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsDao friendsDao;

    @Autowired
    public UserService(@Qualifier("usersDao") UserStorage userStorage, FriendsDao friendsDao) {
        this.userStorage = userStorage;
        this.friendsDao = friendsDao;
    }

    private void validUser(User user) {
        LocalDate now = LocalDate.now();

        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank()
                || !user.getEmail().contains("@")) {
            log.warn("Попытка создать пользователя с пустым или не корректным адресом email");
            throw new ValidationException("Адрес почты введён неверно");
        } else if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()
                || user.getLogin().contains(" ")) {
            log.warn("Попытка создать пользователя с пустым или содержащим пробелы логином");
            throw new ValidationException("Логин не должен быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(now)) {
            log.warn("Попытка создать пользователя с датой рождения из будущего");
            throw new ValidationException("День рождения не может быть из будущего :)");
        }

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.warn("Попытка создать пользователя с пустым именем, вместо имени будет присвоен логин");
            user.setName(user.getLogin());
        }
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validUser(user);
        return userStorage.update(user);
    }

    public User getById(Integer id) {
        return userStorage.getById(id);
    }

    public void addFriendForDb(Integer id, Integer friendId) {
        friendsDao.addFriend(id, friendId);
    }

    public void removeFriendFromDb(Integer id, Integer friendId) {
        friendsDao.removeFriend(id, friendId);
    }

    public Collection<User> getFriendsFromDb(Integer id) {
        return friendsDao.getFriends(id);
    }

    public Collection<User> getCommonFriendsFromDb(Integer id, Integer otherId) {
        return friendsDao.getCommonFriends(id, otherId);
    }
}
