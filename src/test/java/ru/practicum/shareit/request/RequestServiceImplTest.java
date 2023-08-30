package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
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
class RequestServiceImplTest {
    @InjectMocks
    RequestServiceImpl requestService;

    @Mock
    UserRepository userRepository;

    @Mock
    RequestRepository requestRepository;

    UserDto userDto;
    User user;

    RequestDto requestDto;

    Request request;

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

        requestDto = RequestDto.builder()
                .description("Какая - то вещь")
                .build();

        request = Request.builder()
                .id(1L)
                .description("Какая - то вещь")
                .created(LocalDateTime.now())
                .requestor(user)
                .build();
    }

    @Test
    void test1_createNormalRequestDto() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        Mockito.when(requestRepository.save(Mockito.any()))
                .thenReturn(request);

        RequestDto newRequest = requestService.create(requestDto, 1L);

        assertThat(newRequest.getId(), equalTo(1L));
        assertThat(newRequest.getDescription(), equalTo("Какая - то вещь"));
    }

    @Test
    void test2_createRequestDtoWithOutUser() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> requestService.create(requestDto, 1L));
    }

    @Test
    void test3_getAllNormalRequestDto() {
        List<Request> lists = Collections.singletonList(request);

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Mockito.when(requestRepository.searchAllByRequestor_IdOrderByCreatedDesc(1l))
                .thenReturn(Collections.singletonList(request));

        List<RequestDto> listsReq = requestService.getAllUserRequest(1L);

        assertThat(listsReq.size(), equalTo(1));


    }

    @Test
    void test4_getAllRequestDtoWithOutUser() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> requestService.getAllUserRequest(1L));

    }

    @Test
    void test5_getNormalRequestDto() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        Mockito.when(requestRepository.findById(1l))
                .thenReturn(Optional.of(request));
        RequestDto newRequest = requestService.get(1L, 1L);

        assertThat(newRequest.getId(), equalTo(1L));
        assertThat(newRequest.getDescription(), equalTo("Какая - то вещь"));

    }

    @Test
    void test6_getRequestDtoWithOutUser() {

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> requestService.get(1L, 1L));
    }

    @Test
    void test7_getAllNotUserRequestDto() {
        Mockito.when(requestRepository.searchAllByRequestor_IdNot(Mockito.any(), Mockito.any()))
                .thenReturn(Collections.singletonList(request));


        List<RequestDto> listsReq = requestService.getAllNoUserRequest(1L, 0, 1);

        assertThat(listsReq.size(), equalTo(1));

    }

}