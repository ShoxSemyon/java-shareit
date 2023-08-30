package ru.practicum.shareit.user;

import org.h2.mvstore.tx.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest( )
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository userRepository;
    User user;


    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("v@mail.ru")
                .name("Y")
                .build();


    }

    @Test
    void update() {
        em.persist(user);
        em.flush();
        em.clear();
        int updateCount = userRepository.update(user.getId(), null, "X");

        List<User> updateUser = em.getEntityManager()
                .createQuery("select u FROM User u", User.class)
                .getResultList();

        assertThat(updateCount, equalTo(1));
        assertThat(updateUser.get(0).getName(), equalTo("X"));

    }
}