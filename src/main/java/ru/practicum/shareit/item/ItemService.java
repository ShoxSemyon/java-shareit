package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

     ItemDto create(ItemDto itemDto, Long id);

     List<ItemDto> getAll(Long id);

     ItemDto get(Long id);

     List<ItemDto> search(String value);

     ItemDto update(ItemDto itemDto, Long id, Long itemId);

     void delete(Long id, Long itemId);
}
