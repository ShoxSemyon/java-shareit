package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

public class BookingMapper {
    public static Booking convertToBooking(BookingDto bookingDto) {
        return new Booking(bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                null,
                null,
                null);
    }

    public static BookingDto convertToBookingDto(Booking booking) {
        return new BookingDto(booking.getId(),
                booking.getItem().getId(),
                booking.getStartDate(),
                booking.getEndDate(),
                ItemMapper.convertToItem(booking.getItem()),
                UserMapper.converFromDto(booking.getBooker()),
                booking.getBookingStatus());
    }

    public static BookingShortDto convertToBookingShortDto(Booking booking) {
        return new BookingShortDto(booking.getId(),
                booking.getBooker().getId()
        );

    }
}
