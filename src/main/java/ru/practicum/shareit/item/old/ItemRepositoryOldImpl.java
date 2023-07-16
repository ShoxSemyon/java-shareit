package ru.practicum.shareit.item.old;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


public class ItemRepositoryOldImpl implements ItemRepositoryOld {
    private final Map<Long, Item> itemDtoList = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public Item save(Item item) {
        item.setId(++idCounter);
        itemDtoList.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> loadAll(long id) {
        return itemDtoList.values()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public Item load(long id) {
        idExisting(id);
        return itemDtoList.get(id);
    }

    @Override
    public Item update(Item item, long userId) {
        idExisting(item.getId());
        Item oldItem = itemDtoList.get(item.getId());


        if (item.getName() == null || item.getName().isBlank())
            item.setName(oldItem.getName());
        if (item.getDescription() == null || item.getDescription().isBlank())
            item.setDescription(oldItem.getDescription());
        if (item.getAvailable() == null)
            item.setAvailable(oldItem.getAvailable());
        if (item.getRequest() == 0)
            item.setRequest(oldItem.getRequest());


        itemDtoList.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> search(String value) {
        String lowerValue = value.toLowerCase(Locale.ROOT);

        return itemDtoList.values()
                .stream()
                .filter(item -> (item.getName().toLowerCase(Locale.ROOT).contains(lowerValue)
                        || item.getDescription().toLowerCase(Locale.ROOT).contains(lowerValue))
                        && item.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        idExisting(id);

        itemDtoList.remove(id);
    }

    private void idExisting(long id) {
        if (!itemDtoList.containsKey(id))
            throw new NotFoundException(String.format("Вещь с id = %s не найден", id));
    }
}
