package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
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
    public ItemDto create(ItemDto itemDto, Long id) {
        userRepository.load(id);
        Item item = ItemMapper.convertToItemDto(itemDto);
        item.setOwner(id);
        itemRepository.save(item);
        return ItemMapper.convertToItem(item);
    }

    @Override
    public List<ItemDto> getAll(Long id) {
        return itemRepository.loadAll(id)
                .stream()
                .map(ItemMapper::convertToItem)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto get(Long id) {
        return ItemMapper.convertToItem(itemRepository.load(id));
    }

    @Override
    public List<ItemDto> search(String value) {
        if (value == null || value.isBlank()) return new ArrayList<>();

        return itemRepository.search(value)
                .stream()
                .map(ItemMapper::convertToItem)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long id, Long itemId) {
        userRepository.load(id);
        Item item = ItemMapper.convertToItemDto(itemDto);
        item.setId(itemId);
        itemRepository.update(item, id);
        return ItemMapper.convertToItem(item);
    }

    @Override
    public void delete(Long id, Long itemId) {
        userRepository.load(id);
        itemRepository.delete(itemId);
    }
}
