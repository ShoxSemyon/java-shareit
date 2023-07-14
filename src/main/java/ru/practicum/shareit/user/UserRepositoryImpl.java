package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AllreadyExcetprion;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.User;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> userDtoList = new HashMap<>();
    private final Set<String> mailList = new HashSet<>();
    private Long idCounter = 0L;

    @Override
    public User save(User user) {
        emailNotExisting(user.getEmail());
        user.setId(++idCounter);
        mailList.add(user.getEmail());
        userDtoList.put(user.getId(), user);

        return user;
    }

    @Override
    public List<User> loadAll() {
        return new ArrayList<>(userDtoList.values());
    }

    @Override
    public User load(long id) {
        idExisting(id);

        return userDtoList.get(id);
    }

    @Override
    public User update(User user) {
        idExisting(user.getId());

        User oldUser = userDtoList.get(user.getId());
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        } else if (!user.getEmail().equals(oldUser.getEmail())) {
            emailNotExisting(user.getEmail());
            mailList.remove(oldUser.getEmail());
            mailList.add(user.getEmail());
        }
        if (user.getName() == null) user.setName(oldUser.getName());


        userDtoList.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(long id) {
        idExisting(id);
        mailList.remove(userDtoList
                .get(id)
                .getEmail());
        userDtoList.remove(id);
    }

    private void emailNotExisting(String email) {
        if (mailList.contains(email))
            throw new AllreadyExcetprion(String.format("Клиент с email = %s уже существует", email));
    }

    private void idExisting(long id) {
        if (!userDtoList.containsKey(id))
            throw new NotFoundException(String.format("Клиент с id = %s не найден", id));
    }
}
