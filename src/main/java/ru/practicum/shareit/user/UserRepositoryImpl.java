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
        emailExisting(userDto);
        userDto.setId(++idCounter);
        return userDtoList.put(userDto.getId(), userDto);
    }

    @Override
    public List<UserDto> loadAll() {
        return new ArrayList<>(userDtoList.values());
    }

    @Override
    public UserDto load(long id) {
        if (!userDtoList.containsKey(id)) throw new UserNotFoundException();

        return userDtoList.get(id);
    }

    @Override
    public UserDto update(UserDto userDto) {
        if (!userDtoList.containsKey(userDto.getId())) throw new UserNotFoundException();

        UserDto oldUserDto = userDtoList.get(userDto.getId());
        if (userDto.getEmail() == null) userDto.setEmail(oldUserDto.getEmail());
        if (userDto.getName() == null) userDto.setName(oldUserDto.getName());

        emailExisting(userDto);

        return userDtoList.put(userDto.getId(), userDto);
    }

    @Override
    public void delete(long id) {
        if (!userDtoList.containsKey(id)) throw new UserNotFoundException();

        userDtoList.remove(id);
    }

    private void emailExisting(UserDto userDto) {
        Optional<UserDto> optionalUserDto = userDtoList.values()
                .stream()
                .filter(user -> user.getEmail().equals(userDto.getEmail()))
                .findFirst();
        if (optionalUserDto.isPresent()) throw new UserAllreadyExcetprion();
    }
}
