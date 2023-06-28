package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User create(User user);

    User get(Long id);

    List<User> getAll();

    User update(User user, long id);

    void delete(Long id);
}
