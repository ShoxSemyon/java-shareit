package ru.practicum.shareit.user.old;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserRepositoryOld {
    User save(User user);

    List<User> loadAll();

    User load(long id);

    User update(User user);

    void delete(long id);
}
