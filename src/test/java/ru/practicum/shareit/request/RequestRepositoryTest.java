package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.OffsetBasedPageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest()
@Transactional(propagation = Propagation.REQUIRES_NEW)
class RequestRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private RequestRepository requestRepository;

    User user;

    User secondUser;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("y@mail.ru")
                .name("Y")
                .build();
        em.persist(user);

        secondUser = User.builder()
                .email("z@mail.ru")
                .name("Z")
                .build();
        em.persist(secondUser);

        em.persist(Request.builder()
                .requestor(user)
                .description("X")
                .created(LocalDateTime.now())
                .build());

        em.persist(Request.builder()
                .requestor(secondUser)
                .description("X")
                .created(LocalDateTime.now())
                .build());

    }

    @Test
    void searchAllByRequestor_IdOrderByCreatedDesc() {
        List<Request> requestList = requestRepository
                .searchAllByRequestor_IdOrderByCreatedDesc(user.getId());

        assertThat(requestList.size(), equalTo(1));
    }

    @Test
    void searchAllByRequestor_IdNot() {
        List<Request> requestList = requestRepository
                .searchAllByRequestor_IdNot(user.getId(), new OffsetBasedPageRequest(0, 10));

        assertThat(requestList.size(), equalTo(1));
    }
}