package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final EntityManager entityManager;


    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserServiceImpl.exceptionFormat(id));
        Item item = ItemMapper.convertToItemDto(itemDto);
        item.setOwner(user);
        itemRepository.save(item);
        return ItemMapper.convertToItem(item);
    }

    @Override
    public List<ItemDto> getAll(Long id) {
        return itemRepository.findAllByOwnerId(id)
                .stream()
                .map(ItemMapper::convertToItem)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto get(Long id) {
        return ItemMapper.convertToItem(itemRepository.findById(id)
                .orElseThrow(() -> exceptionFormat(id)));
    }

    @Override
    @Transactional
    public List<ItemDto> search(String value) {
        if (value == null || value.isBlank()) return new ArrayList<>();

        List<Item> items = itemRepository.searchByValue(value);

        return items.stream()
                .map(ItemMapper::convertToItem)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, Long id, Long itemId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserServiceImpl.exceptionFormat(id));
        Item item = ItemMapper.convertToItemDto(itemDto);
        item.setId(itemId);
        item.setOwner(user);

        if (itemRepository.update(item.getId(),
                item.getOwner().getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest()) < 1) throw exceptionFormat(itemId);

        log.info("Вещь c id {} обновлена", item.getId());

        return ItemMapper.convertToItem(itemRepository.findById(itemId).get());
    }

    @Override
    public void delete(Long id, Long itemId) {
        userRepository.findById(id)
                .orElseThrow(() -> UserServiceImpl.exceptionFormat(id));
        itemRepository.deleteById(itemId);
    }

    public static NotFoundException exceptionFormat(Long id) {
        return new NotFoundException(String.format("Вещь с id = %s, не найден", id));
    }
}
