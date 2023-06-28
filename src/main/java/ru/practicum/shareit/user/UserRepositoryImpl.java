package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, UserDto> userDtoList = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public UserDto save(UserDto userDto) {
        emailNotExisting(userDto);
        userDto.setId(++idCounter);
        userDtoList.put(userDto.getId(), userDto);

        return userDto;
    }

    @Override
    public List<UserDto> loadAll() {
        return new ArrayList<>(userDtoList.values());
    }

    @Override
    public UserDto load(long id) {
        idExisting(id);

        return userDtoList.get(id);
    }

    @Override
    public UserDto update(UserDto userDto) {
        idExisting(userDto.getId());

        UserDto oldUserDto = userDtoList.get(userDto.getId());
        if (userDto.getEmail() == null) {
            userDto.setEmail(oldUserDto.getEmail());
        } else {
            emailNotExisting(userDto);
        }
        if (userDto.getName() == null) userDto.setName(oldUserDto.getName());


        userDtoList.put(userDto.getId(), userDto);
        return userDto;
    }

    @Override
    public void delete(long id) {
        idExisting(id);

        userDtoList.remove(id);
    }

    private void emailNotExisting(UserDto userDto) {
        Optional<UserDto> optionalUserDto = userDtoList.values()
                .stream()
                .filter(user -> user.getEmail().equals(userDto.getEmail()))
                .findFirst();
        if (optionalUserDto.isPresent() && userDto.getId() != optionalUserDto.get().getId())
            throw new UserAllreadyExcetprion(String.format("Клиент с email = %s уже существует", userDto.getEmail()));
    }

    private void idExisting(long id) {
        if (!userDtoList.containsKey(id))
            throw new UserNotFoundException(String.format("Клиент с id = %s не найден", id));
    }
}
