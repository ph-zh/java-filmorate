package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {
    public Collection<User> findAll();

    User create(User user);

    User update(User user);

    User getById(Integer id);

    void addFriendForDb(Integer id, Integer friendId);

    void removeFriendFromDb(Integer id, Integer friendId);

    Collection<User> getFriendsFromDb(Integer id);

    Collection<User> getCommonFriendsFromDb(Integer id, Integer otherId);
}