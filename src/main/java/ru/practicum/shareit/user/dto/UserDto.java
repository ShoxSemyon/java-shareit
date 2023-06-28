package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.User;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Slf4j
public class UserDto {
    long id;//— уникальный идентификатор пользователя;

    String name; //— имя или логин пользователя;

    String email;//— адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).

    public static UserDto createUserDto(User user) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail());
    }
}

