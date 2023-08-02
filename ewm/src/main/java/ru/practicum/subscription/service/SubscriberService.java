package ru.practicum.subscription.service;

import ru.practicum.subscription.model.Subscriber;
import ru.practicum.user.model.User;

import java.util.List;

public interface SubscriberService {

    void subscribeToAuthor(User subscriber, User author);

    void unSubscribeFromAuthor(User subscriber, User author);

    List<Subscriber> getSubscribersForAuthor(User author);

    List<Subscriber> getAuthorsForSubscriber(User subscriber);
}