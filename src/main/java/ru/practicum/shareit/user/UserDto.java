package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotNull(groups = UpdateGroupMarker.UpdateMarker.class)
    long id;//— уникальный идентификатор пользователя;

    @NotBlank(groups = UserCreateGroupMarker.UserCreateMarker.class)
    String name; //— имя или логин пользователя;

    @NotNull(groups = UserCreateGroupMarker.UserCreateMarker.class)
    @Email
    String email;//— адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).

}
