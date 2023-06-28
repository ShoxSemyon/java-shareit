package ru.practicum.shareit.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAllreadyExcetprion extends RuntimeException {
    public UserAllreadyExcetprion(String msg) {
        super(msg);
    }
}
