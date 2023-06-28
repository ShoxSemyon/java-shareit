package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserAllreadyExcetprion extends RuntimeException {
    String msg;

}
