package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {
    long id;// — уникальный идентификатор вещи;

    @NotBlank
    String name;// — краткое название;

    @NotBlank
    String description;//— развёрнутое описание;

    @NotNull
    Boolean available;// — статус о том, доступна или нет вещь для аренды;

    long owner;// — владелец вещи;
    long request;// — если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.

    public static Item convertToItem(ItemDto itemDto) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner(),
                itemDto.getRequest());
    }
}
