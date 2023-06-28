package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, ItemDto> itemDtoList = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public ItemDto save(ItemDto itemDto) {
        itemDto.setId(++idCounter);
        itemDtoList.put(itemDto.getId(), itemDto);
        return itemDto;
    }

    @Override
    public List<ItemDto> loadAll(long id) {
        return itemDtoList.values()
                .stream()
                .filter(itemDto -> itemDto.getOwner() == id)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto load(long id) {
        idExisting(id);
        return itemDtoList.get(id);
    }

    @Override
    public ItemDto update(ItemDto itemDto, long userId) {
        idExisting(itemDto.getId());
        ItemDto oldItemDto = itemDtoList.get(itemDto.getId());

        if (oldItemDto.getOwner() != userId) throw new ItemNotFoundException();
        itemDto.setOwner(oldItemDto.getOwner());

        if (itemDto.getName() == null) itemDto.setName(oldItemDto.getName());
        if (itemDto.getDescription() == null) itemDto.setDescription(oldItemDto.getDescription());
        if (itemDto.getAvailable() == null) itemDto.setAvailable(oldItemDto.getAvailable());
        if (itemDto.getRequest() == 0) itemDto.setRequest(oldItemDto.getRequest());


        itemDtoList.put(itemDto.getId(), itemDto);
        return itemDto;
    }

    @Override
    public List<ItemDto> search(String value) {
        return itemDtoList.values()
                .stream()
                .filter(itemDto -> (itemDto.getName().toLowerCase().contains(value.toLowerCase())
                        || itemDto.getDescription().toLowerCase().contains(value.toLowerCase()))
                        && itemDto.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        idExisting(id);

        itemDtoList.remove(id);
    }

    private void idExisting(long id) {
        if (!itemDtoList.containsKey(id))
            throw new ItemNotFoundException(String.format("Вещь с id = %s не найден", id));
    }
}
