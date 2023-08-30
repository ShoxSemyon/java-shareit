package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.OffsetBasedPageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest()
@Transactional(propagation = Propagation.REQUIRES_NEW)
class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;
    User booker;
    User owner;
    Item item;
    Request request;
    Booking booking;
    Comment comment;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .email("1@ya.ru")
                .name("Ivan")
                .build();
        em.persist(owner);

        item = Item.builder()
                .description("X")
                .request(request)
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

        request = Request.builder()
                .requestor(booker)
                .created(LocalDateTime.now())
                .items(Collections.singletonList(item))
                .description("R")
                .build();
        em.persist(request);

        booking = Booking.builder()
                .booker(booker)
                .item(item)
                .bookingStatus(BookingStatus.WAITING)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2L))
                .build();
        em.persist(booking);

        comment = Comment.builder()
                .text("C")
                .authorName(booker)
                .item(item)
                .created(LocalDateTime.now())
                .build();
        em.persist(comment);

    }


    @Test
    void test1_findAllByOwnerId() {
        List<Item> items = itemRepository.findAllByOwnerId(owner.getId(),
                new OffsetBasedPageRequest(0, 10));

        assertThat(items.size(), equalTo(1));
    }

    @Test
    void test2_findByIdAndAndAvailable() {
        Optional<Item> item1 = itemRepository.findByIdAndAndAvailable(item.getId(),
                item.getAvailable());
        assertThat(item1.get().getId(), equalTo(item.getId()));
    }

    @Test
    void test3_update() {
        itemRepository.update(item.getId(),
                item.getOwner().getId(),
                "newName",
                null,
                null,
                null);
        em.clear();
        em.flush();

        assertThat(itemRepository
                        .findById(item.getId())
                        .get()
                        .getName(),
                equalTo("newName"));
    }

    @Test
    void searchByValue() {
        assertThat(itemRepository
                        .searchByValue(item.getName(),
                                new OffsetBasedPageRequest(0, 10))
                        .size(),
                equalTo(1));
    }

    @Test
    void deleteByIdAndOwner_Id() {
        assertThat(itemRepository
                        .deleteByIdAndOwner_Id(item.getId(),
                                item.getOwner().getId()),
                equalTo(1));
    }
}