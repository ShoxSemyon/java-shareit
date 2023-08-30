package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.OffsetBasedPageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest()
@Transactional(propagation = Propagation.REQUIRES_NEW)
class BookingRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;
    User owner;
    Item item;
    User booker;
    Booking booking;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .email("1@ya.ru")
                .name("Ivan")
                .build();
        em.persist(owner);

        item = Item.builder()
                .description("X")
                .available(true)
                .owner(owner)
                .name("X")
                .build();
        em.persist(item);

        booker = User.builder()
                .email("2@ya.ru")
                .name("Stepan")
                .build();
        em.persist(booker);

        booking = Booking.builder()
                .booker(booker)
                .item(item)
                .bookingStatus(BookingStatus.WAITING)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2L))
                .build();
        em.persist(booking);
    }

    @Test
    void test1_findAllByBooker_IdOrderByStartDateDesc() {
        List<Booking> lists = bookingRepository.findAllByBooker_IdOrderByStartDateDesc(booker.getId(),
                new OffsetBasedPageRequest(0, 10));

        assertThat(lists.size(), equalTo(1));
    }

    @Test
    void test2_findAllByBooker_IdAndAndEndDateBeforeOrderByStartDateDesc() {
        List<Booking> lists = bookingRepository
                .findAllByBooker_IdAndAndEndDateBeforeOrderByStartDateDesc(booker.getId(),
                        LocalDateTime.now().plusDays(3),
                        new OffsetBasedPageRequest(0, 10));

        assertThat(lists.size(), equalTo(1));
    }

    @Test
    void test3_findAllByBooker_IdAndAndStartDateAfterOrderByStartDateDesc() {
        List<Booking> lists = bookingRepository
                .findAllByBooker_IdAndAndStartDateAfterOrderByStartDateDesc(booker.getId(),
                        LocalDateTime.now().minusDays(3),
                        new OffsetBasedPageRequest(0, 10));

        assertThat(lists.size(), equalTo(1));
    }

    @Test
    void test4_findAllByBooker_IdAndBookingStatusOrderByStartDateDesc() {
        List<Booking> lists = bookingRepository
                .findAllByBooker_IdAndBookingStatusOrderByStartDateDesc(booker.getId(),
                        BookingStatus.WAITING,
                        new OffsetBasedPageRequest(0, 10));

        assertThat(lists.size(), equalTo(1));

    }

    @Test
    void test5_findAllByBooker_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc() {
        List<Booking> lists = bookingRepository
                .findAllByBooker_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(booker.getId(),
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1),
                        new OffsetBasedPageRequest(0, 10));

        assertThat(lists.size(), equalTo(1));
    }

    @Test
    void test6_findAllByItem_Owner_IdOrderByStartDateDesc() {
        List<Booking> lists = bookingRepository.findAllByItem_Owner_IdOrderByStartDateDesc(owner.getId(),
                new OffsetBasedPageRequest(0, 10));

        assertThat(lists.size(), equalTo(1));
    }

    @Test
    void test7_findAllByItem_Owner_IdAndAndEndDateBeforeOrderByStartDateDesc() {
        List<Booking> lists = bookingRepository
                .findAllByItem_Owner_IdAndAndEndDateBeforeOrderByStartDateDesc(owner.getId(),
                        LocalDateTime.now().plusDays(3),
                        new OffsetBasedPageRequest(0, 10));

        assertThat(lists.size(), equalTo(1));
    }

    @Test
    void test8_findAllByItem_Owner_IdAndAndStartDateAfterOrderByStartDateDesc() {
        List<Booking> lists = bookingRepository
                .findAllByItem_Owner_IdAndAndStartDateAfterOrderByStartDateDesc(owner.getId(),
                        LocalDateTime.now().minusDays(3),
                        new OffsetBasedPageRequest(0, 10));

        assertThat(lists.size(), equalTo(1));
    }

    @Test
    void test9_findAllByItem_Owner_IdAndBookingStatusOrderByStartDateDesc() {
        List<Booking> lists = bookingRepository
                .findAllByItem_Owner_IdAndBookingStatusOrderByStartDateDesc(owner.getId(),
                        BookingStatus.WAITING,
                        new OffsetBasedPageRequest(0, 10));

        assertThat(lists.size(), equalTo(1));
    }

    @Test
    void test10_findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc() {
        List<Booking> lists = bookingRepository
                .findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(owner.getId(),
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1),
                        new OffsetBasedPageRequest(0, 10));

        assertThat(lists.size(), equalTo(1));
    }

    @Test
    void test11_findTop1ByItem_IdAndStartDateIsBeforeAndBookingStatusOrderByStartDateDesc() {
        Optional<Booking> bookingOptional = bookingRepository
                .findTop1ByItem_IdAndStartDateIsBeforeAndBookingStatusOrderByStartDateDesc(item.getId(),
                        LocalDateTime.now().plusDays(1),
                        BookingStatus.WAITING);

        assertThat(bookingOptional.get().getId(), equalTo(booking.getId()));
    }

    @Test
    void findTop1ByItem_IdAndStartDateIsAfterAndBookingStatusOrderByStartDateAsc() {
        Optional<Booking> bookingOptional = bookingRepository
                .findTop1ByItem_IdAndStartDateIsAfterAndBookingStatusOrderByStartDateAsc(item.getId(),
                        LocalDateTime.now().minusDays(1),
                        BookingStatus.WAITING);

        assertThat(bookingOptional.get().getId(), equalTo(booking.getId()));
    }

    @Test
    void countAllByBooker_IdAndItem_IdAndBookingStatusAndStartDateIsBefore() {
        Integer count = bookingRepository
                .countAllByBooker_IdAndItem_IdAndBookingStatusAndStartDateIsBefore(booker.getId(),
                        item.getId(),
                        BookingStatus.WAITING,
                        LocalDateTime.now().plusDays(1));

        assertThat(count, equalTo(1));
    }
}