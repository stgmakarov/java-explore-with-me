package ru.practicum.request.service;

import ru.practicum.event.dto.EventInDto;
import ru.practicum.event.dto.EventReqStOutDto;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface RequestService {

    ParticipationRequestDto createNewRequestFromUser(Integer userId, Integer eventId);

    Integer getConfirmedRequest(Integer eventId);

    Collection<ParticipationRequestDto> getRequestListForParticipationEvent(Integer userId, Integer eventId);

    Collection<ParticipationRequestDto> getUserRequestList(Integer userId);

    ParticipationRequestDto cancelParticipationRequest(Integer userId, Integer requestId);

    EventReqStOutDto changeStateUserRequests(Integer userId, Integer eventId,
                                             EventInDto requests);
}
