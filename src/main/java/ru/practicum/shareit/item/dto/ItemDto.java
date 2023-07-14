package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.ItemUpdateMarker;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @NotNull(groups = ItemUpdateMarker.class)
    long id;// — уникальный идентификатор вещи;

    @NotBlank
    String name;// — краткое название;

    @NotBlank
    String description;//— развёрнутое описание;

    @NotNull
    Boolean available;// — статус о том, доступна или нет вещь для аренды;

    long owner;// — владелец вещи;
    long request;// — если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.

}
