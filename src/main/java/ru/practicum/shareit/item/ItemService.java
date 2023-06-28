package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

     Item create(Item item, Long id);

     List<Item> getAll(Long id);

     Item get(Long id);

     List<Item> search(String value);

     Item update(Item item, Long id, Long itemId);

     void delete(Long id, Long itemId);
}
