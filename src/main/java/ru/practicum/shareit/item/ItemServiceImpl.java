package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailibleException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;


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
                .map(item -> {
                    ItemDto itemDto = ItemMapper.convertToItem(item);
                    if (itemDto.getOwner() == id) {
                        addLastAndNextBooking(itemDto);
                    }
                    addComments(itemDto);
                    return itemDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto get(Long id, Long userId) {

        ItemDto itemDto = ItemMapper.convertToItem(itemRepository
                .findById(id)
                .orElseThrow(() -> exceptionFormat(id)));

        if (itemDto.getOwner() == userId) {
            addLastAndNextBooking(itemDto);
        }
        addComments(itemDto);
        return itemDto;

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

    @Override
    @Transactional
    public CommentDto addComment(CommentDto commentDto, long userId, long itemId) {
        if (bookingRepository.countAllByBooker_IdAndItem_Id(userId, itemId) < 1)
            throw new UnavailibleException("Нелья оcтавить комментарий");

        Comment comment = new Comment();
        comment.setText(commentDto.getText());

        comment.setItem(itemRepository.findById(itemId).get());
        comment.setAuthorName(userRepository.findById(userId).get());

        comment.setCreated(LocalDateTime.now());

        return CommentMapper.convertToCommentDto(commentRepository.save(comment));
    }

    public static NotFoundException exceptionFormat(Long id) {
        return new NotFoundException(String.format("Вещь с id = %s, не найден", id));
    }

    private void addLastAndNextBooking(ItemDto itemDto) {
        Optional<Booking> last = bookingRepository
                .findTop1ByItem_IdAndEndDateIsBeforeOrderByEndDateDesc(itemDto.getId(), LocalDateTime.now());

        last.ifPresent(book -> itemDto
                .setLastBooking(BookingMapper.convertToBookingShortDto(book)));

        Optional<Booking> next = bookingRepository
                .findTop1ByItem_IdAndStartDateIsAfterOrderByStartDateAsc(itemDto.getId(), LocalDateTime.now());

        next.ifPresent(book -> itemDto
                .setNextBooking(BookingMapper.convertToBookingShortDto(book)));

    }

    private void addComments(ItemDto itemDto) {
        List<CommentDto> comments = commentRepository.
                findAllByItem_Id(itemDto.getId())
                .stream()
                .map(CommentMapper::convertToCommentDto)
                .collect(Collectors.toList());

        itemDto.setComments(comments);

    }
}

