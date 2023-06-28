package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item create(Item item, Long id) {
        userRepository.load(id);
        ItemDto itemDto = ItemDto.convertToItemDto(item);
        itemDto.setOwner(id);
        itemRepository.save(itemDto);
        return Item.convertToItem(itemDto);
    }

    @Override
    public List<Item> getAll(Long id) {
        return itemRepository.loadAll(id)
                .stream()
                .map(Item::convertToItem)
                .collect(Collectors.toList());
    }

    @Override
    public Item get(Long id) {
        return Item.convertToItem(itemRepository.load(id));
    }

    @Override
    public List<Item> search(String value) {
        if (value == null || value.isBlank()) return new ArrayList<>();

        return itemRepository.search(value)
                .stream()
                .map(Item::convertToItem)
                .collect(Collectors.toList());
    }

    @Override
    public Item update(Item item, Long id, Long itemId) {
        userRepository.load(id);
        ItemDto itemDto = ItemDto.convertToItemDto(item);
        itemDto.setId(itemId);
        itemRepository.update(itemDto, id);
        return Item.convertToItem(itemDto);
    }

    @Override
    public void delete(Long id, Long itemId) {
        userRepository.load(id);
        itemRepository.delete(itemId);
    }
}
