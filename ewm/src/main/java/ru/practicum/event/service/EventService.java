package ru.practicum.event.service;

import ru.practicum.event.dto.EventOutDto;
import ru.practicum.event.dto.EventShortOutDto;
import ru.practicum.event.dto.NewEventInDto;
import ru.practicum.event.model.Event;
import ru.practicum.state.SortState;
import ru.practicum.state.State;
import ru.practicum.user.dto.UserEventInDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {
    EventOutDto createNewEvent(Integer userId, NewEventInDto eventDto);

    Event getEventById(Integer eventId);

    Collection<Event> getEventListByEventIds(Collection<Integer> ids);

    EventOutDto saveChangeEventForAdmin(Event event);

    Collection<EventShortOutDto> getEventShortListWithSort(Collection<Event> events, Boolean onlyAvailable);

    EventOutDto getFullEventById(Integer id, String ip);

    EventOutDto changeEventByUser(Integer userId, Integer eventId,
                                  UserEventInDto eventUserRequest);

    EventOutDto findFounderEvent(Integer userId, Integer eventId);

    Collection<EventOutDto> findEventsWithParameters(List<Integer> users, List<State> states,
                                                     List<Integer> categories, LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd, Integer from, Integer size);

    Collection<EventShortOutDto> findAllFounderEvents(Integer userId, Integer from, Integer size);

    Collection<EventShortOutDto> findPublicEventsWithParameters(String text, Collection<Integer> categories,
                                                                Boolean paid, String rangeStart, String rangeEnd,
                                                                Boolean onlyAvailable, SortState sort, Integer from,
                                                                Integer size, String ip);
}
