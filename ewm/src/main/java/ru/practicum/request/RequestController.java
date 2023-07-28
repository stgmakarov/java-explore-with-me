package ru.practicum.request;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventInDto;
import ru.practicum.event.dto.EventReqStOutDto;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@AllArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> getRequestListForParticipationEvent(@PathVariable Integer userId,
                                                                                   @PathVariable Integer eventId) {
        return requestService.getRequestListForParticipationEvent(userId, eventId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createNewRequestFromUser(@PathVariable @Positive Integer userId,
                                                            @RequestParam(value = "eventId") @Positive
                                                            Integer eventId) {
        return requestService.createNewRequestFromUser(userId, eventId);
    }

    @GetMapping("/users/{userId}/requests")
    public Collection<ParticipationRequestDto> getUserRequestList(@PathVariable Integer userId) {
        return requestService.getUserRequestList(userId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable Integer userId,
                                                              @PathVariable Integer requestId) {
        return requestService.cancelParticipationRequest(userId, requestId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventReqStOutDto changeStateUserRequests(@PathVariable Integer userId,
                                                    @PathVariable Integer eventId,
                                                    @RequestBody
                                                    EventInDto requests) {
        return requestService.changeStateUserRequests(userId, eventId, requests);
    }
}
