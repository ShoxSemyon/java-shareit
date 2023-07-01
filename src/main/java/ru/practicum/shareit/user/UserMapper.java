package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.User;

public class UserMapper {
    public static User createUserDto(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }

    public static UserDto converFromDto(User user) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail());
    }

}
