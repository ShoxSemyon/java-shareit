package ru.practicum.shareit.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AllreadyExcetprion extends RuntimeException {
    public AllreadyExcetprion(String msg) {
        super(msg);
    }
}
