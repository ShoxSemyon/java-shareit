package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserNotFoundException extends RuntimeException {
    String msg;
}
