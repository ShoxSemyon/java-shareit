package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item save(Item item);

    List<Item> loadAll(long id);

    Item load(long id);

    Item update(Item item, long userId);

    List<Item> search(String value);

    void delete(long id);
}
