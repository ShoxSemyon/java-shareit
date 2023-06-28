package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public User create(
            @Validated({ValidateMarker.class})
            @RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable
                    @NotNull
                    Long id) {
        return userService.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable
                       @NotNull
                       Long id) {
        userService.delete(id);
    }

    @PatchMapping("/{id}")
    public User update(
            @Validated(Default.class)
            @RequestBody
            User user,
            @PathVariable
            @NotNull
            Long id) {
        return userService.update(user, id);
    }
}
