package ru.practicum.shareit.user;

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
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    UserDto userDto;
    User user;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .email("1@ya.ru")
                .name("Ivan")
                .build();

        user = User.builder()
                .id(1)
                .email("1@ya.ru")
                .name("Ivan")
                .build();
    }

    @Test
    void test1_createNormalUserDto() {
        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(user);
        UserDto saveUserDto = userService.create(userDto);

        assertThat(saveUserDto.getId(), equalTo(1L));
        assertThat(saveUserDto.getEmail(), equalTo("1@ya.ru"));
        assertThat(saveUserDto.getName(), equalTo("Ivan"));
    }

    @Test
    void test2_getAllUserDto() {
        List<User> lists = Collections.singletonList(user);

        Mockito.when(userRepository.findAll())
                .thenReturn(lists);

        List<UserDto> userDtoList = userService.getAll();

        assertThat(userDtoList.size(), equalTo(1));
        assertThat(userDtoList.get(0).getId(), equalTo(1L));

    }

    @Test
    void test3_getExistUser() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        UserDto saveUserDto = userService.get(1L);

        assertThat(saveUserDto.getId(), equalTo(1L));
        assertThat(saveUserDto.getEmail(), equalTo("1@ya.ru"));
        assertThat(saveUserDto.getName(), equalTo("Ivan"));
    }

    @Test
    void test4_getNotExistUser() {
        Mockito.when(userRepository.findById(2L))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> userService.get(2L));
    }

    @Test
    void test5_deleteExistUser() {
        userService.delete(1L);
        Mockito.verify(userRepository).deleteById(1L);

    }


    @Test
    void test6_updateExistUser() {
        UserDto newUserDto = UserDto.builder()
                .email("tralala@mail.ru")
                .build();

        User newUser = User.builder()
                .email("tralala@mail.ru")
                .name("Ivan")
                .id(1L)
                .build();

        Mockito.when(userRepository.update(Mockito.any(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(1);
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(newUser));

        UserDto savaUserDto = userService.update(newUserDto, 1L);

        assertThat(savaUserDto.getId(), equalTo(1L));
        assertThat(savaUserDto.getEmail(), equalTo("tralala@mail.ru"));
        assertThat(savaUserDto.getName(), equalTo("Ivan"));
    }

    @Test
    void test7_updateNotExistUser() {
        UserDto newUserDto = UserDto.builder()
                .email("tralala@mail.ru")
                .build();

        Mockito.when(userRepository.update(Mockito.any(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(0);

        assertThrows(NotFoundException.class,
                () -> userService.update(newUserDto, 1L));
    }

}