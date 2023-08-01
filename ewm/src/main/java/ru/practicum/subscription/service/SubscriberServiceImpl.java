package ru.practicum.subscription.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.error.RequestError;
import ru.practicum.subscriber.repository.SubscriberRepository;
import ru.practicum.subscription.model.Subscriber;
import ru.practicum.user.model.User;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository subscriberRepository;

    @Override
    @Transactional
    public void subscribeToAuthor(User subscriber, User author) {
        checkUserNotSubscribeForAuthor(subscriber, author);
        Subscriber sub = new Subscriber();
        sub.setSubscriber(subscriber);
        sub.setAuthor(author);
        subscriberRepository.save(sub);
        log.info("Пользователь {} подписался на пользователя {}", subscriber, author);
    }

    @Override
    @Transactional
    public void unSubscribeFromAuthor(User subscriber, User author) {
        if (!checkUserSubscribeForAuthor(subscriber, author)) {
            log.info("Пользователь {} не может отписаться от пользователя {}, " +
                    "так как он не является его подписчиком", subscriber, author);
            throw new RequestError(HttpStatus.CONFLICT, "Невозможно отписаться," +
                    " пользователь " + subscriber + " не является подписчиком " + author);
        }
        Subscriber subscriberNow = subscriberRepository
                .getSubscriberBySubscriberAndAuthor(subscriber, author);
        subscriberRepository.deleteSubById(subscriberNow.getId());
        log.info("Пользователь {} отписался от пользователя {}", subscriber, author);
    }

    @Override
    public List<Subscriber> getSubscribersForAuthor(User author) {
        log.info("Запрошен список подписчиков пользователя {}", author);
        return subscriberRepository.getSubscribersByAuthor(author);
    }

    @Override
    public List<Subscriber> getAuthorsForSubscriber(User subscriber) {
        log.info("Запрошен список подписок пользователя {}", subscriber);
        return subscriberRepository.getAuthorsBySubscriber(subscriber);
    }

    private void checkUserNotSubscribeForAuthor(User sub, User aut) {
        Collection<Subscriber> subscribers = getSubscribersForAuthor(aut);
        subscribers.forEach(subscriber -> {
            if (subscriber.getSubscriber().getId().equals(sub.getId())) {
                log.info("Невозможно подписаться на {}, " +
                        "т.к. {} уже является подписчиком", aut, sub);
                throw new RequestError(HttpStatus.CONFLICT, "Невозможно подписаться " +
                        "на пользователя " + aut + ", пользователь = " + sub +
                        " уже подписчик ");
            }
        });
    }

    private boolean checkUserSubscribeForAuthor(User sub, User author) {
        Collection<Subscriber> subscribers = getSubscribersForAuthor(author);
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getSubscriber().getId().equals(sub.getId())) {
                return true;
            }
        }
        return false;
    }
}