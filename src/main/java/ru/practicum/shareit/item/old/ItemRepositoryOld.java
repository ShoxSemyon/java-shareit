package ru.practicum.shareit.item.old;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepositoryOld {
    Item save(Item item);

    List<Item> loadAll(long id);

    Item load(long id);

    Item update(Item item, long userId);

    List<Item> search(String value);

    void delete(long id);
}
