package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker_IdOrderByStartDateDesc(
            long userId);

    List<Booking> findAllByBooker_IdAndAndEndDateBeforeOrderByStartDateDesc(
            long userId,
            LocalDateTime localDateTime);

    List<Booking> findAllByBooker_IdAndAndStartDateAfterOrderByStartDateDesc(
            long userId,
            LocalDateTime localDateTime);

    List<Booking> findAllByBooker_IdAndBookingStatusOrderByStartDateDesc(
            long userId,
            BookingStatus status);

    List<Booking> findAllByBooker_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
            long userId,
            LocalDateTime localDateTime1,
            LocalDateTime localDateTime2);

    List<Booking> findAllByItem_Owner_IdOrderByStartDateDesc(
            long ownerId);

    List<Booking> findAllByItem_Owner_IdAndAndEndDateBeforeOrderByStartDateDesc(
            long ownerId,
            LocalDateTime localDateTime);

    List<Booking> findAllByItem_Owner_IdAndAndStartDateAfterOrderByStartDateDesc(
            long ownerId,
            LocalDateTime localDateTime);

    List<Booking> findAllByItem_Owner_IdAndBookingStatusOrderByStartDateDesc(
            long ownerId,
            BookingStatus status);

    List<Booking> findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
            long ownerId,
            LocalDateTime localDateTime1,
            LocalDateTime localDateTime2);

    Optional<Booking> findTop1ByItem_IdAndStartDateIsBeforeAndBookingStatusOrderByStartDateDesc(
            long itemId,
            LocalDateTime localDateTime,
            BookingStatus status);

    Optional<Booking> findTop1ByItem_IdAndStartDateIsAfterAndBookingStatusOrderByStartDateAsc(
            long itemId,
            LocalDateTime localDateTime,
            BookingStatus status);

    Integer countAllByBooker_IdAndItem_IdAndBookingStatusAndStartDateIsBefore(
            long ownerId,
            long itemId,
            BookingStatus status,
            LocalDateTime localDateTime);

}
