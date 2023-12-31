package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Validated
@Slf4j
public class UserController {
    private UserService userService;

    @PostMapping
    public UserDto create(
            @Validated({UserCreateGroupMarker.class})
            @RequestBody UserDto userDto) {
        log.info("Начало сохранение пользователя {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Начало запроса всех пользователей");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable
                       @NotNull
                       Long id) {
        log.info("Начало запроса  пользователя с id={}", id);
        return userService.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable
                       @NotNull
                       Long id) {
        log.info("Начало удаления  пользователя с id={}", id);
        userService.delete(id);
    }

    @PatchMapping("/{id}")
    public UserDto update(
            @Validated(UpdateGroupMarker.class)
            @RequestBody
            UserDto userDto,
            @PathVariable
            @NotNull
            Long id) {
        log.info("Начало обновления  пользователя {} с id={}", userDto, id);
        return userService.update(userDto, id);
    }
}
