package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingStatus;
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

        log.info("Вещь {} пользователя id={} сохранена",
                itemDto,
                id);

        return ItemMapper.convertToItem(item);
    }

    @Override
    public List<ItemDto> getAll(Long id) {
        log.info("Вещи для пользователя id={} сохранена",
                id);

        return itemRepository.findAllByOwnerId(id)
                .stream()
                .map(item -> {
                    ItemDto itemDto = ItemMapper.convertToItem(item);
                    addComments(itemDto);
                    if (itemDto.getOwner() == id) {
                        addLastAndNextBooking(itemDto);
                    }
                    return itemDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto get(Long id, Long userId) {

        ItemDto itemDto = ItemMapper.convertToItem(itemRepository
                .findById(id)
                .orElseThrow(() -> exceptionFormat(id)));

        addComments(itemDto);
        if (itemDto.getOwner() == userId) {
            addLastAndNextBooking(itemDto);
        }

        log.info("Вещь id= {} для пользователя id={}",
                userId,
                id);

        return itemDto;

    }

    @Override
    @Transactional
    public List<ItemDto> search(String value) {
        if (value == null || value.isBlank()) return new ArrayList<>();

        log.info("Вещи c описанием или названием = {}",
                value);

        return itemRepository.searchByValue(value)
                .stream()
                .map(ItemMapper::convertToItem)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, Long id, Long itemId) {

        Item item = ItemMapper.convertToItemDto(itemDto);
        item.setId(itemId);

        if (itemRepository.update(item.getId(),
                id,
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest()) < 1) throw exceptionFormat(itemId);

        log.info("Вещь c id {} обновлена",
                item.getId());

        return ItemMapper.convertToItem(itemRepository.findById(itemId).get());
    }

    @Override
    public void delete(Long id, Long itemId) {

        if (itemRepository.deleteByIdAndOwner_Id(id, itemId) == null) {
            throw exceptionFormat(itemId);
        }
        log.info("Вещь c id {} удалена",
                itemId);
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentDto commentDto, long userId, long itemId) {
        if (bookingRepository.countAllByBooker_IdAndItem_IdAndBookingStatusAndStartDateIsBefore(userId,
                itemId,
                BookingStatus.APPROVED,
                LocalDateTime.now()) < 1)
            throw new UnavailibleException("Нелья оcтавить комментарий");

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(itemRepository.findById(itemId).get());
        comment.setAuthorName(userRepository.findById(userId).get());
        comment.setCreated(LocalDateTime.now());

        log.info("Комментарий c id {} добавлен",
                commentDto.getId());

        return CommentMapper.convertToCommentDto(commentRepository.save(comment));
    }

    public static NotFoundException exceptionFormat(Long id) {
        return new NotFoundException(String.format("Вещь с id = %s, не найден", id));
    }

    private void addLastAndNextBooking(ItemDto itemDto) {
        bookingRepository
                .findTop1ByItem_IdAndStartDateIsBeforeAndBookingStatusOrderByStartDateDesc(itemDto.getId(),
                        LocalDateTime.now(),
                        BookingStatus.APPROVED)
                .ifPresent(book -> itemDto
                        .setLastBooking(BookingMapper.convertToBookingShortDto(book)));

        bookingRepository
                .findTop1ByItem_IdAndStartDateIsAfterAndBookingStatusOrderByStartDateAsc(itemDto.getId(),
                        LocalDateTime.now(),
                        BookingStatus.APPROVED)
                .ifPresent(book -> itemDto
                        .setNextBooking(BookingMapper.convertToBookingShortDto(book)));

    }

    private void addComments(ItemDto itemDto) {
        List<CommentDto> comments = commentRepository
                .findAllByItem_Id(itemDto.getId())
                .stream()
                .map(CommentMapper::convertToCommentDto)
                .collect(Collectors.toList());

        itemDto.setComments(comments);

    }
}

