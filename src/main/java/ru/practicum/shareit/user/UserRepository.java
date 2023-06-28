package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserRepository {
    UserDto save(UserDto userDto);

    List<UserDto> loadAll();

    UserDto load(long id);

    UserDto update(UserDto userDto);

    void delete(long id);
}
