package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {
    ItemDto save(ItemDto itemDto);

    List<ItemDto> loadAll(long id);

    ItemDto load(long id);

    ItemDto update(ItemDto itemDto, long userId);

    List<ItemDto> search(String value);

    void delete(long id);
}
