package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        UserDto userDto = userRepository.save(UserDto.createUserDto(user));
        log.info("Пользователь добавлен {}", userDto);
        return User.converFromDto(userDto);
    }

    @Override
    public User get(Long id) {
        return User.converFromDto(userRepository.load(id));
    }

    @Override
    public List<User> getAll() {
        return userRepository.loadAll()
                .stream()
                .map(User::converFromDto)
                .collect(Collectors.toList());
    }

    @Override
    public User update(User user, long id) {
        UserDto userDto = UserDto.createUserDto(user);
        userDto.setId(id);
        log.info("Пользователь обновлён {}", userDto);
        return User.converFromDto(userRepository.update(userDto));
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }
}
