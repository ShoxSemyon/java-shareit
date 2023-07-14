package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.User;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.save(UserMapper.createUserDto(userDto));
        log.info("Пользователь добавлен {}", user);
        return UserMapper.converFromDto(user);
    }

    @Override
    public UserDto get(Long id) {
        return UserMapper.converFromDto(userRepository.load(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.loadAll()
                .stream()
                .map(UserMapper::converFromDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        User user = UserMapper.createUserDto(userDto);
        user.setId(id);
        log.info("Пользователь обновлён {}", user);
        return UserMapper.converFromDto(userRepository.update(user));
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }
}
