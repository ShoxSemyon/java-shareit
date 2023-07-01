package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    List<User> loadAll();

    User load(long id);

    User update(User user);

    void delete(long id);
}
