package ru.practicum.shareit.item;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String format) {
        super(format);
    }
}
