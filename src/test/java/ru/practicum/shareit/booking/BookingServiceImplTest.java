package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailibleException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
class BookingServiceImplTest {
    @InjectMocks
    BookingServiceImpl bookingService;

    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;

    User booker;
    Item item;

    Booking booking;

    BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        User owner = User.builder()
                .id(1L)
                .email("1@ya.ru")
                .name("Ivan")
                .build();

        item = Item.builder()
                .id(1L)
                .description("X")
                .available(true)
                .owner(owner)
                .name("X")
                .build();

        booker = User.builder()
                .id(2L)
                .email("2@ya.ru")
                .name("Stepan")
                .build();

        bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(2))
                .build();

        booking = Booking.builder()
                .id(1L)
                .booker(booker)
                .item(item)
                .bookingStatus(BookingStatus.WAITING)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2L))
                .build();
    }

    @Test
    void test1_createNormalBookingDto() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(booker));

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        when(bookingRepository.save(any()))
                .thenReturn(booking);


        BookingDto saveBookingDto = bookingService.create(bookingDto,
                2L);

        assertThat(saveBookingDto.getId(), equalTo(1L));
        assertThat(saveBookingDto.getItem().getId(), equalTo(1L));
        assertThat(saveBookingDto.getBooker().getId(), equalTo(2L));
    }

    @Test
    void test2_createBookingDtoWithOutUser() {

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingDto,
                2L));
    }

    @Test
    void test3_createBookingDtoWithOutItem() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(booker));
        assertThrows(NotFoundException.class, () -> bookingService.create(bookingDto,
                2L));
    }

    @Test
    void test4_createBookingDtoWithNotAvalibleItem() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(booker));

        item.setAvailable(false);
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        assertThrows(UnavailibleException.class, () -> bookingService.create(bookingDto,
                2L));
    }

    @Test
    void test5_createBookingDtoByOwner() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(booker));

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingDto,
                1L));
    }

    @Test
    void test6_updateNormalBookingDto() {

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        when(bookingRepository.save(any()))
                .thenReturn(booking);

        BookingDto updateBooking = bookingService.update(1L,
                true,
                1L);

        assertThat(updateBooking.getId(), equalTo(1L));
        assertThat(updateBooking.getItem().getId(), equalTo(1L));


    }

    @Test
    void test7_updateBookingDtoNotOwner() {

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class,
                () -> bookingService.update(1L, true, 2L));

    }

    @Test
    void test8_updateBookingDtoNotWating() {

        booking.setBookingStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        assertThrows(UnavailibleException.class,
                () -> bookingService.update(1L, true, 1L));

    }

    @Test
    void test9_getNormalBookingDto() {

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        BookingDto updateBooking = bookingService.get(1L, 1L);

        assertThat(updateBooking.getId(), equalTo(1L));
        assertThat(updateBooking.getItem().getId(), equalTo(1L));

    }

    @Test
    void test10_getNotExistBookingDto() {

        assertThrows(NotFoundException.class,
                () -> bookingService.get(1L, 1L));

    }

    @Test
    void test11_getBookingDtoNotOwnerOrBooker() {

        assertThrows(NotFoundException.class,
                () -> bookingService.get(1L, 3L));

    }

    @Test
    void test12_getAllNormalBookingDtoUser() {

        when(bookingRepository.findAllByBooker_IdOrderByStartDateDesc(anyLong(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingDto> bookingDtoList = bookingService.getAllForUser(1L,
                BookingState.ALL,
                0,
                10);

        assertThat(bookingDtoList.size(), equalTo(1));

    }

    @Test
    void test13_getAllEmptyBookingDtoUser() {

        assertThrows(NotFoundException.class,
                () -> bookingService.getAllForUser(1L,
                        BookingState.ALL,
                        0,
                        10));
    }

    @Test
    void test14_getAllNormalBookingDtoOwner() {

        when(bookingRepository.findAllByItem_Owner_IdOrderByStartDateDesc(anyLong(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingDto> bookingDtoList = bookingService.getAllForOwner(1L,
                BookingState.ALL,
                0,
                10);

        assertThat(bookingDtoList.size(), equalTo(1));

    }

    @Test
    void test15_getAllEmptyBookingDtoOwner() {

        assertThrows(NotFoundException.class,
                () -> bookingService.getAllForOwner(1L,
                        BookingState.ALL,
                        0,
                        10));
    }

    @Test
    void test16_getAllNormalBookingDtoUser() {

        when(bookingRepository.findAllByBooker_IdAndAndEndDateBeforeOrderByStartDateDesc(anyLong(),
                any(),
                any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingDto> bookingDtoList = bookingService.getAllForUser(1L,
                BookingState.PAST,
                0,
                10);

        assertThat(bookingDtoList.size(), equalTo(1));

    }

    @Test
    void test17_getAllNormalBookingDtoUser() {

        when(bookingRepository.findAllByBooker_IdAndAndStartDateAfterOrderByStartDateDesc(anyLong(),
                any(),
                any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingDto> bookingDtoList = bookingService.getAllForUser(1L,
                BookingState.FUTURE,
                0,
                10);

        assertThat(bookingDtoList.size(), equalTo(1));

    }

    @Test
    void test18_getAllNormalBookingDtoUser() {

        when(bookingRepository.findAllByBooker_IdAndBookingStatusOrderByStartDateDesc(anyLong(),
                any(),
                any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingDto> bookingDtoList = bookingService.getAllForUser(1L,
                BookingState.WAITING,
                0,
                10);

        assertThat(bookingDtoList.size(), equalTo(1));

    }

    @Test
    void test19_getAllNormalBookingDtoUser() {

        when(bookingRepository.findAllByBooker_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(anyLong(),
                any(),
                any(),
                any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingDto> bookingDtoList = bookingService.getAllForUser(1L,
                BookingState.CURRENT,
                0,
                10);

        assertThat(bookingDtoList.size(), equalTo(1));

    }

    @Test
    void test20_getAllNormalBookingDtoOwner() {

        when(bookingRepository.findAllByItem_Owner_IdAndAndEndDateBeforeOrderByStartDateDesc(anyLong(),
                any(),
                any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingDto> bookingDtoList = bookingService.getAllForOwner(1L,
                BookingState.PAST,
                0,
                10);

        assertThat(bookingDtoList.size(), equalTo(1));

    }

    @Test
    void test21_getAllNormalBookingDtoOwner() {

        when(bookingRepository.findAllByItem_Owner_IdAndBookingStatusOrderByStartDateDesc(anyLong(),
                any(),
                any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingDto> bookingDtoList = bookingService.getAllForOwner(1L,
                BookingState.WAITING,
                0,
                10);

        assertThat(bookingDtoList.size(), equalTo(1));

    }

    @Test
    void test22_getAllNormalBookingDtoOwner() {

        when(bookingRepository.findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(anyLong(),
                any(),
                any(),
                any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingDto> bookingDtoList = bookingService.getAllForOwner(1L,
                BookingState.CURRENT,
                0,
                10);

        assertThat(bookingDtoList.size(), equalTo(1));

    }


}