package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    long id;//— уникальный идентификатор пользователя;

    String name; //— имя или логин пользователя;

    String email;//— адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).

}

