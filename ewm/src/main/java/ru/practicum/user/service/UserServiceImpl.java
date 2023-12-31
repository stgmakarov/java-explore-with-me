package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.error.RequestError;
import ru.practicum.subscription.service.SubscriberService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserInDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriberService subscriberService;

    @Override
    @Transactional
    public UserDto addNewUser(UserInDto userInDto) {
        log.info("Добавление нового пользователя {} в базу данных", userInDto);
        User newUser = userRepository.save(UserMapper.toUser(userInDto));
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public Collection<UserDto> getAllUser(List<Integer> ids) {
        log.info("Запрос на получение пользователей из списка: {}", ids);
        Collection<User> users;
        if (ids != null)
            users = userRepository.getUsersByIds(ids);
        else
            users = userRepository.getAllUsers();
        Collection<UserDto> usersDto = new ArrayList<>();
        users.forEach(user -> usersDto.add(UserMapper.toUserDto(user)));
        return usersDto;
    }

    @Override
    public Collection<UserDto> getAllUserWithPagination(List<Integer> ids, Integer from, Integer size) {
        log.info("Запрос на получение пользователей из списка : {} с использованием пагинации", ids);
        from = from / size;
        Page<User> users;
        if (ids != null)
            users = userRepository.getUsersByIdsWithPagination(ids, PageRequest.of(from, size));
        else
            users = userRepository.getAllUsersWithPagination(PageRequest.of(from, size));
        Collection<UserDto> usersDto = new ArrayList<>();
        users.forEach(user -> usersDto.add(UserMapper.toUserDto(user)));
        return usersDto;
    }

    @Override
    public User getUserById(Integer userId) {
        log.info("Запрос на получение пользователя под id {}", userId);
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.info("Неверно указан ID пользователя {} для удаления", userId);
            throw new RequestError(HttpStatus.NOT_FOUND, "Пользователь с id " + userId + " не найден");
        }
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.info("Неверно указан ID пользователя {} для удаления", userId);
            throw new RequestError(HttpStatus.NOT_FOUND, "Пользователь с id " + userId + " не найден");
        }
        log.info("Пользователь {} удален", user);
        userRepository.deleteById(userId);
    }

    @Override
    public Collection<UserShortDto> getSubscribersForAuthor(Integer authorId) {
        User user = getUserById(authorId);
        log.info("Список подписчиков пользователя {}", user);
        List<UserShortDto> subList = new ArrayList<>();
        subscriberService.getSubscribersForAuthor(user).forEach(subscriber ->
                subList.add(UserMapper.toUserShortDto(subscriber.getSubscriber())));
        return subList;
    }

    @Override
    @Transactional
    public void subscribeToAuthor(Integer userId, Integer authorId) {
        User subscriber = getUserById(userId);
        User author = getUserById(authorId);
        log.info("Запрос от пользователя {} на подписку на пользователя {}",
                subscriber, author);
        subscriberService.subscribeToAuthor(subscriber, author);
    }

    @Override
    @Transactional
    public void unSubscribeFromAuthor(Integer userId, Integer authorId) {
        User subscriber = getUserById(userId);
        User author = getUserById(authorId);
        log.info("Запрос от пользователя {} на отписку от обновлений пользователя {}",
                subscriber, author);
        subscriberService.unSubscribeFromAuthor(subscriber, author);
    }
}
