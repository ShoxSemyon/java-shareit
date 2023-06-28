package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
   private final ItemService itemService;

    @PostMapping
    public Item create(@Valid @RequestBody Item item,
                       @RequestHeader("X-Sharer-User-Id") @NotNull Long id) {
        return itemService.create(item, id);
    }

    @GetMapping
    public List<Item> getAll(@RequestHeader("X-Sharer-User-Id") @NotNull Long id) {
        return itemService.getAll(id);
    }

    @GetMapping("/{id}")
    public Item get(@PathVariable @NotNull Long id) {
        return itemService.get(id);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam("text") String value) {
        return itemService.search(value);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestBody Item item,
                       @RequestHeader("X-Sharer-User-Id") @NotNull Long id,
                       @PathVariable @NotNull Long itemId) {
        return itemService.update(item, id, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") @NotNull Long id,
                       @PathVariable @NotNull Long itemId) {
        itemService.delete(id, itemId);
    }
}
