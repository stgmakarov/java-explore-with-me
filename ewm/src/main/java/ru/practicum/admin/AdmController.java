package ru.practicum.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.dto.AdmCompInDto;
import ru.practicum.compilation.dto.CompOutDto;
import ru.practicum.compilation.dto.CompInDto;
import ru.practicum.admin.dto.EventAdminInDto;
import ru.practicum.admin.service.AdmService;
import ru.practicum.category.dto.CategoryOutDto;
import ru.practicum.category.dto.CategoryInDto;
import ru.practicum.event.dto.EventOutDto;
import ru.practicum.state.State;
import ru.practicum.user.dto.UserInDto;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdmController {
    @Autowired
    private AdmService admService;

    @PostMapping("/categories")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryOutDto createNewCategory(@RequestBody @Valid CategoryInDto categoryInDto) {
        return admService.createNewCategory(categoryInDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer catId) {
        admService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryOutDto changeCategory(@PathVariable Integer catId,
                                         @RequestBody @Valid CategoryInDto categoryInDto) {
        return admService.changeCategory(catId, categoryInDto);
    }

    @GetMapping("/events")
    public Collection<EventOutDto> getAllEvents(@RequestParam(value = "users", required = false)
                                                 List<Integer> users,
                                                @RequestParam(value = "states", required = false)
                                                 List<State> states,
                                                @RequestParam(value = "categories", required = false)
                                                 List<Integer> categories,
                                                @RequestParam(value = "rangeStart", required = false)
                                                 String rangeStart,
                                                @RequestParam(value = "rangeEnd", required = false)
                                                 String rangeEnd,
                                                @PositiveOrZero
                                                 @RequestParam(value = "from", defaultValue = "0", required = false)
                                                 Integer from,
                                                @Positive
                                                 @RequestParam(value = "size", defaultValue = "10", required = false)
                                                 Integer size) {

        return admService.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventOutDto changeEvent(@PathVariable Integer eventId,
                                   @RequestBody @Valid EventAdminInDto eventAdminInDto) {

        return admService.changeEvent(eventId, eventAdminInDto);
    }

    @GetMapping("/users")
    public Collection<UserDto> getAllUsers(@RequestParam(value = "ids", required = false) List<Integer> ids,
                                           @PositiveOrZero
                                           @RequestParam(value = "from", required = false, defaultValue = "0")
                                           Integer from,
                                           @Positive
                                           @RequestParam(value = "size", required = false, defaultValue = "10")
                                           Integer size) {
        return admService.getAllUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto createNewUser(@RequestBody @Valid UserInDto userInDto) {
        return admService.createUser(userInDto);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        admService.deleteUser(userId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompOutDto createNewCompilation(@RequestBody @Valid CompInDto compInDto) {
        return admService.createNewCompilation(compInDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer compId) {
        admService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompOutDto updateCompilation(@PathVariable Integer compId,
                                        @RequestBody @Valid AdmCompInDto compilationRequest) {
        return admService.updateCompilation(compId, compilationRequest);
    }
}
