package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailibleException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
class ItemServiceImplTest {

    @InjectMocks
    ItemServiceImpl itemService;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    RequestRepository requestRepository;
    User booker;
    User owner;
    Item item;

    Request request;
    ItemDto itemDto;
    Booking booking;
    Comment comment;
    CommentDto commentDto;

    @BeforeEach
    void setUp() {

        owner = User.builder()
                .id(1L)
                .email("1@ya.ru")
                .name("Ivan")
                .build();

        item = Item.builder()
                .id(1L)
                .description("X")
                .request(request)
                .available(true)
                .owner(owner)
                .name("X")
                .build();

        itemDto = ItemDto.builder()
                .requestId(1L)
                .description("X")
                .available(true)
                .name("X")
                .build();

        booker = User.builder()
                .id(2L)
                .email("2@ya.ru")
                .name("Stepan")
                .build();

        request = Request.builder()
                .id(1L)
                .requestor(booker)
                .created(LocalDateTime.now())
                .items(Collections.singletonList(item))
                .description("R")
                .build();

        booking = Booking.builder()
                .id(1L)
                .booker(booker)
                .item(item)
                .bookingStatus(BookingStatus.WAITING)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2L))
                .build();

        comment = Comment.builder()
                .id(1L)
                .text("C")
                .authorName(booker)
                .item(item)
                .created(LocalDateTime.now())
                .build();

        commentDto = CommentDto.builder()
                .text("C")
                .build();


    }

    @Test
    void test1_createNormalItemDto() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(owner));
        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(request));
        when(itemRepository.save(any()))
                .thenReturn(item);

        ItemDto saveItemDto = itemService.create(itemDto, 1L);

        assertThat(saveItemDto.getId(), equalTo(item.getId()));
    }

    @Test
    void test2_createItemDtoWithOutUser() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.create(itemDto, 1L));
    }

    @Test
    void test3_createItemDtoWithOutRequest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(owner));

        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.create(itemDto, 1L));
    }

    @Test
    void test4_getAllNormalDto() {
        when(itemRepository.findAllByOwnerId(anyLong(),
                any()))
                .thenReturn(Collections.singletonList(item));

        List<ItemDto> itemDtos = itemService.getAll(1L,
                0,
                10);
        assertThat(itemDtos.get(0).getId(), equalTo(1L));
    }

    @Test
    void test5_updateNormalItemDto() {
        when(itemRepository.update(anyLong(),
                anyLong(),
                anyString(),
                anyString(),
                anyBoolean(),
                anyLong()))
                .thenReturn(1);

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        ItemDto saveItemDto = itemService.update(itemDto,
                1L,
                1L);

        assertThat(saveItemDto.getId(), equalTo(item.getId()));
    }

    @Test
    void test6_updateNullRawItemDto() {
        when(itemRepository.update(anyLong(),
                anyLong(),
                anyString(),
                anyString(),
                anyBoolean(),
                anyLong()))
                .thenReturn(1);

        assertThrows(NoSuchElementException.class,
                () -> itemService.update(itemDto,
                        1L,
                        1L));
    }

    @Test
    void test7_deleteNormalItemDto() {
        when(itemRepository.deleteByIdAndOwner_Id(anyLong(),
                anyLong()))
                .thenReturn(1);

        itemService.delete(1L, 1L);

        Mockito.verify(itemRepository).deleteByIdAndOwner_Id(1L, 1L);
    }

    @Test
    void test8_deleteBadItemDto() {
        when(itemRepository.deleteByIdAndOwner_Id(anyLong(),
                anyLong()))
                .thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> itemService.delete(1L,
                        1L));

    }

    @Test
    void test9_getNormalItemDto() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findTop1ByItem_IdAndStartDateIsBeforeAndBookingStatusOrderByStartDateDesc(anyLong(),
                any(),
                any()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.findTop1ByItem_IdAndStartDateIsAfterAndBookingStatusOrderByStartDateAsc(anyLong(),
                any(),
                any()))
                .thenReturn(Optional.of(booking));

        when(commentRepository.findAllByItem_Id(anyLong()))
                .thenReturn(Collections.singletonList(comment));

        ItemDto getItemDto = itemService.get(1L, 1L);

        assertThat(getItemDto.getId(), equalTo(item.getId()));
        assertThat(getItemDto.getComments().get(0).getId(), equalTo(item.getId()));
        assertThat(getItemDto.getLastBooking().getId(), equalTo(item.getId()));

    }

    @Test
    void test10_getNotExistItemDto() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.get(1L,
                        1L));

    }

    @Test
    void test11_searchNormalItemDto() {
        when(itemRepository.searchByValue(anyString(),
                any()))
                .thenReturn(Collections.singletonList(item));

        List<ItemDto> itemDtos = itemService.search("GG",
                0,
                10);
        assertThat(itemDtos.get(0).getId(), equalTo(1L));

    }

    @Test
    void test12_addNormalCommentDto() {
        when(bookingRepository.countAllByBooker_IdAndItem_IdAndBookingStatusAndStartDateIsBefore(anyLong(),
                anyLong(),
                any(),
                any()))
                .thenReturn(1);

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(owner));

        when(commentRepository.save(any()))
                .thenReturn(comment);

        CommentDto saveComment = itemService.addComment(commentDto, 1L, 1L);

        assertThat(saveComment.getId(), equalTo(1L));

    }

    @Test
    void test13_addCommentDtoWithOutComment() {
        when(bookingRepository.countAllByBooker_IdAndItem_IdAndBookingStatusAndStartDateIsBefore(anyLong(),
                anyLong(),
                any(),
                any()))
                .thenReturn(0);

        assertThrows(UnavailibleException.class,
                () -> itemService.addComment(commentDto,
                        1L,
                        1L));

    }


}