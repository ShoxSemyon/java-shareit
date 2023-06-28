package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class User {
    long id;//— уникальный идентификатор пользователя;

    @NotBlank(groups = CreateMarker.class)
    String name; //— имя или логин пользователя;

    @NotNull(groups = CreateMarker.class)
    @Email
    String email;//— адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).

    public static User converFromDto(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }
}
