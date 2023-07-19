package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailibleException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingState.*;
import static ru.practicum.shareit.booking.dto.BookingStatus.APPROVED;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingDto create(BookingDto bookingDto, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> UserServiceImpl.exceptionFormat(userId));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> ItemServiceImpl.exceptionFormat(bookingDto.getItemId()));

        if (!item.getAvailable())
            throw exceptionFormatUnavailable("Вещь", userId);

        if (userId == item.getOwner().getId()) throw ItemServiceImpl.exceptionFormat(item.getId());

        Booking booking = BookingMapper.convertToBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setBookingStatus(BookingStatus.WAITING);
        log.info("Бронь {} создана  для клиента id={}", bookingDto, userId);
        return BookingMapper.convertToBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto update(Long bookingId, Boolean approved, Long ownerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> exceptionformatNotFound(bookingId));

        if (booking.getItem().getOwner().getId() != ownerId) {
            throw exceptionformatNotFound(booking.getItem().getId());
        }
        if (!booking.getBookingStatus().equals(BookingStatus.WAITING))
            throw exceptionFormatUnavailable("Бронт", bookingId);

        booking.setBookingStatus(approved ? APPROVED : BookingStatus.REJECTED);
        log.info("Статус для брони {} изменён владельцем id={}", bookingId, ownerId);

        return BookingMapper.convertToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto get(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> exceptionformatNotFound(bookingId));

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId)
            throw exceptionformatNotFound(bookingId);

        return BookingMapper.convertToBookingDto(booking);
    }


    @Override
    public List<BookingDto> getAllForUser(Long userId, BookingState state) {
        List<Booking> bookings = new ArrayList<>();

        if (state.equals(ALL)) bookings = bookingRepository.findAllByBooker_IdOrderByStartDateDesc(userId);
        if (state.equals(PAST))
            bookings = bookingRepository
                    .findAllByBooker_IdAndAndEndDateBeforeOrderByStartDateDesc(userId, LocalDateTime.now());
        if (state.equals(FUTURE))
            bookings = bookingRepository
                    .findAllByBooker_IdAndAndStartDateAfterOrderByStartDateDesc(userId, LocalDateTime.now());

        if (state.equals(WAITING) || state.equals(REJECTED))
            bookings = bookingRepository
                    .findAllByBooker_IdAndBookingStatusOrderByStartDateDesc(userId,
                            BookingStatus.valueOf(state.name()));

        if (state.equals(CURRENT))
            bookings = bookingRepository
                    .findAllByBooker_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(userId,
                            LocalDateTime.now(),
                            LocalDateTime.now());

        if (bookings.size() == 0) throw new NotFoundException();

        return bookings.stream()
                .map(BookingMapper::convertToBookingDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<BookingDto> getAllForOwner(Long ownerId, BookingState state) {
        List<Booking> bookings = new ArrayList<>();

        if (state.equals(ALL))
            bookings = bookingRepository.findAllByItem_Owner_IdOrderByStartDateDesc(ownerId);
        if (state.equals(PAST))
            bookings = bookingRepository
                    .findAllByItem_Owner_IdAndAndEndDateBeforeOrderByStartDateDesc(ownerId,
                            LocalDateTime.now());
        if (state.equals(FUTURE))
            bookings = bookingRepository
                    .findAllByItem_Owner_IdAndAndStartDateAfterOrderByStartDateDesc(ownerId,
                            LocalDateTime.now());

        if (state.equals(WAITING) || state.equals(REJECTED))
            bookings = bookingRepository
                    .findAllByItem_Owner_IdAndBookingStatusOrderByStartDateDesc(ownerId,
                            BookingStatus.valueOf(state.name()));

        if (state.equals(CURRENT))
            bookings = bookingRepository
                    .findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(ownerId,
                            LocalDateTime.now(),
                            LocalDateTime.now());

        if (bookings.size() == 0) throw new NotFoundException();

        return bookings.stream()
                .map(BookingMapper::convertToBookingDto)
                .collect(Collectors.toList());
    }

    public static NotFoundException exceptionformatNotFound(Long id) {
        return new NotFoundException(String.format("Бронь с id = %s, не найден", id));
    }

    public static UnavailibleException exceptionFormatUnavailable(String resorce, long id) {
        return new UnavailibleException(String.format("%s с id %s недоступна", resorce, id));
    }
}
