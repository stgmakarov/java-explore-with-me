package ru.practicum.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.admin.dto.EventAdminInDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventOutDto;
import ru.practicum.event.dto.EventReqStOutDto;
import ru.practicum.event.dto.EventShortOutDto;
import ru.practicum.event.dto.NewEventInDto;
import ru.practicum.event.model.Event;
import ru.practicum.location.model.Location;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@UtilityClass
public class EventMapper {

    public static Event toEvent(User user, Category category, Location location, NewEventInDto newEventInDto) {
        Event event = new Event();
        event.setCreatedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        event.setAnnotation(newEventInDto.getAnnotation());
        event.setEventDate(newEventInDto.getEventDate());
        event.setDescription(newEventInDto.getDescription());
        event.setParticipantLimit(newEventInDto.getParticipantLimit() != null ? newEventInDto.getParticipantLimit() : 0);
        event.setRequestModeration(newEventInDto.getRequestModeration() != null ? newEventInDto.getRequestModeration() : true);
        event.setTitle(newEventInDto.getTitle());
        event.setPaid(newEventInDto.isPaid());
        event.setInitiator(user);
        event.setLocation(location);
        event.setCategory(category);
        return event;
    }

    public static EventOutDto toEventOutDto(Event event, Integer confirmedRequests, Integer views) {
        EventOutDto eventOutDto = new EventOutDto();
        eventOutDto.setConfirmedRequests(Objects
                .requireNonNullElse(confirmedRequests, 0));
        eventOutDto.setViews(Objects.requireNonNullElse(views, 0));
        eventOutDto.setId(event.getId());
        eventOutDto.setDescription(event.getDescription());
        eventOutDto.setAnnotation(event.getAnnotation());
        eventOutDto.setState(event.getState());
        eventOutDto.setCategory(event.getCategory());
        eventOutDto.setPublishedOn(event.getPublishedOn());
        eventOutDto.setInitiator(event.getInitiator());
        eventOutDto.setLocation(event.getLocation());
        eventOutDto.setPaid(event.isPaid());
        eventOutDto.setTitle(event.getTitle());
        eventOutDto.setCreatedOn(event.getCreatedOn());
        eventOutDto.setEventDate(event.getEventDate());
        eventOutDto.setRequestModeration(event.isRequestModeration());
        eventOutDto.setParticipantLimit(event.getParticipantLimit());
        return eventOutDto;
    }

    public static NewEventInDto toNewEventInDto(EventAdminInDto updateEvent) {
        NewEventInDto newEventInDto = new NewEventInDto();
        newEventInDto.setAnnotation(updateEvent.getAnnotation());
        newEventInDto.setDescription(updateEvent.getDescription());
        newEventInDto.setPaid(updateEvent.isPaid());
        newEventInDto.setLocation(updateEvent.getLocation());
        newEventInDto.setCategory(updateEvent.getCategory());
        newEventInDto.setParticipantLimit(updateEvent.getParticipantLimit());
        newEventInDto.setRequestModeration(updateEvent.isRequestModeration());
        newEventInDto.setTitle(updateEvent.getTitle());
        newEventInDto.setEventDate(updateEvent.getEventDate());
        return newEventInDto;
    }

    public static EventShortOutDto toEventShortDto(Event event, Integer confirmedRequest, Integer views) {
        EventShortOutDto eventShortOutDto = new EventShortOutDto();
        eventShortOutDto.setId(event.getId());
        eventShortOutDto.setAnnotation(event.getAnnotation());
        eventShortOutDto.setEventDate(event.getEventDate());
        eventShortOutDto.setPaid(event.isPaid());
        eventShortOutDto.setConfirmedRequests(confirmedRequest);
        eventShortOutDto.setViews(views);
        eventShortOutDto.setTitle(event.getTitle());
        eventShortOutDto.setCategory(CategoryMapper.toCategoryOutDto(event.getCategory()));
        eventShortOutDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        return eventShortOutDto;
    }


    public static EventReqStOutDto toEventRequestStatusUpdateResult(Collection<Request> acceptRequest,
                                                                    Collection<Request> rejectRequest) {
        Collection<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        Collection<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        acceptRequest.forEach(request ->
                confirmedRequests.add(RequestMapper.toParticipationRequestDto(request)));
        rejectRequest.forEach(request ->
                rejectedRequests.add(RequestMapper.toParticipationRequestDto(request)));
        return new EventReqStOutDto(confirmedRequests, rejectedRequests);
    }
}
