package ru.practicum.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventOutDto;
import ru.practicum.event.dto.EventShortOutDto;
import ru.practicum.event.dto.NewEventInDto;
import ru.practicum.event.service.EventService;
import ru.practicum.user.dto.UserEventInDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/events")
    public Collection<EventShortOutDto> findAllFounderEvents(@PathVariable Integer userId,
                                                             @PositiveOrZero
                                                             @RequestParam(value = "from", defaultValue = "0",
                                                                     required = false) Integer from,
                                                             @Positive
                                                             @RequestParam(value = "size", defaultValue = "10",
                                                                     required = false)
                                                             Integer size) {
        return eventService.findAllFounderEvents(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/events")
    public EventOutDto createNewEventByUser(@PathVariable Integer userId,
                                            @RequestBody @Valid NewEventInDto eventDto) {
        return eventService.createNewEvent(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventOutDto findFounderEvent(@PathVariable Integer userId,
                                        @PathVariable Integer eventId) {
        return eventService.findFounderEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventOutDto changeEventByUser(@PathVariable Integer userId,
                                         @PathVariable Integer eventId,
                                         @RequestBody @Valid UserEventInDto eventUserRequest) {
        return eventService.changeEventByUser(userId, eventId, eventUserRequest);
    }

    /** подписка на пользователя
     * @param userId
     * @param authorId
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/subscribe/{authorId}")
    public void subscribeToUser(@PathVariable Integer userId,
                                @PathVariable Integer authorId) {

        userService.subscribeToAuthor(userId, authorId);
    }

    /**получение всех подписчиков пользователя
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/subscribers")
    public Collection<UserShortDto> getSubscribersForUser(@PathVariable Integer userId) {
        return userService.getSubscribersForAuthor(userId);
    }

    /**отписка от пользователя
     * @param userId
     * @param authorId
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/unsubscribe/{authorId}")
    public void unSubscribeFromAuthor(@PathVariable Integer userId,
                                      @PathVariable Integer authorId) {
        userService.unSubscribeFromAuthor(userId, authorId);
    }
}
