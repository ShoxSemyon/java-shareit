package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {

    long id;// — уникальный идентификатор вещи;
    String name;// — краткое название;
    String description;//— развёрнутое описание;
    Boolean available;// — статус о том, доступна или нет вещь для аренды;
    long owner;// — владелец вещи;
    long request;//
}
