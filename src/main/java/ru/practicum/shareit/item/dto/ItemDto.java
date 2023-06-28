package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemDto {

    long id;// — уникальный идентификатор вещи;
    String name;// — краткое название;
    String description;//— развёрнутое описание;
    Boolean available;// — статус о том, доступна или нет вещь для аренды;
    long owner;// — владелец вещи;
    long request;//

    public static ItemDto convertToItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest());
    }
}
