package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.util.WebConfig;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig({RequestController.class, RequestControllerTestConfig.class, WebConfig.class})
class RequestControllerTest {
    private final ObjectMapper mapper;

    private final RequestService requestService;

    private MockMvc mvc;

    private RequestDto requestDto;

    @Autowired
    public RequestControllerTest(RequestService requestService, ObjectMapper mapper) {
        this.requestService = requestService;
        this.mapper = mapper;
    }

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

        requestDto = RequestDto.builder()
                .id(1L)
                .description("D")
                .created(LocalDateTime.now())
                .build();

    }

    @Test
    @SneakyThrows
    void test1_createNormalRequestDto() {
        when(requestService.create(any(), anyLong()))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.created", notNullValue()));
    }

    @Test
    @SneakyThrows
    void test2_getNormalUserRequestDto() {
        when(requestService.get(anyLong(), anyLong()))
                .thenReturn(requestDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.created", notNullValue()));
    }

    @Test
    @SneakyThrows
    void test3_getAllNormalUserRequestDto() {
        when(requestService.getAllUserRequest(anyLong()))
                .thenReturn(Collections.singletonList(requestDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description",
                        is(requestDto.getDescription())));

    }

    @SneakyThrows
    @Test
    void test4_getAllNormalNoUserRequestDto() {
        when(requestService.getAllNoUserRequest(anyLong(),anyInt(),anyInt()))
                .thenReturn(Collections.singletonList(requestDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                       )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description",
                        is(requestDto.getDescription())));
    }
}