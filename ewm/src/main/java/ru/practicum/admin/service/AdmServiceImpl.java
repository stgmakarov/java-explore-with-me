package ru.practicum.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.AdmCompInDto;
import ru.practicum.common.GlobalConsts;
import ru.practicum.compilation.dto.CompOutDto;
import ru.practicum.compilation.dto.CompInDto;
import ru.practicum.admin.dto.EventAdminInDto;
import ru.practicum.category.dto.CategoryOutDto;
import ru.practicum.category.dto.CategoryInDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.compilation.mapper.CompMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompRepository;
import ru.practicum.compilation.service.CompService;
import ru.practicum.error.RequestError;
import ru.practicum.event.dto.EventOutDto;
import ru.practicum.event.dto.NewEventInDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.location.model.Location;
import ru.practicum.location.service.LocationService;
import ru.practicum.state.ActionState;
import ru.practicum.state.State;
import ru.practicum.user.dto.UserInDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AdmServiceImpl implements AdmService {
    @Autowired
    private LocationService locationService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private EventService eventService;
    @Autowired
    private CompService compService;
    @Autowired
    private CompRepository compRepository;
    DateTimeFormatter dateTimeFormatter = GlobalConsts.getDateTimeFormatter();

    @Override
    public CategoryOutDto createNewCategory(CategoryInDto categoryInDto) {
        log.info("adm.createNewCategory {}", categoryInDto);
        return categoryService.create(categoryInDto);
    }

    @Override
    public CategoryOutDto changeCategory(Integer catId, CategoryInDto categoryInDto) {
        log.info("adm.changeCategory {}", catId);
        return categoryService.update(catId, categoryInDto);
    }

    @Override
    public UserDto createUser(UserInDto userInDto) {
        log.info("adm.createUser {}", userInDto);
        return userService.addNewUser(userInDto);
    }

    @Override
    public Collection<UserDto> getAllUsers(List<Integer> ids, Integer from, Integer size) {
        log.info("adm.getAllUsers {}", ids);
        if (from == null && size == null) {
            return userService.getAllUser(ids);
        } else {
            return userService.getAllUserWithPagination(ids, from, size);
        }
    }

    @Override
    public EventOutDto changeEvent(Integer eventId, EventAdminInDto eventAdminInDto) {
        Event oldEvent = eventService.getEventById(eventId);
        if (oldEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            log.info("Дата начала события должна быть не ранее чем за час от даты публикации");
            throw new RequestError(HttpStatus.CONFLICT, "Дата начала события должна быть " +
                    "не ранее чем за час от даты публикации");
        }

        if (oldEvent.getState() != State.PENDING) {
            log.info("Невозможно изменить статус. Текущий статус {}", oldEvent.getState());
            throw new RequestError(HttpStatus.CONFLICT, "Невозможно изменить статус");
        }

        if (eventAdminInDto.getEventDate() != null &&
                eventAdminInDto.getEventDate().isBefore(LocalDateTime.now())) {
            log.info("Невозможно изменить событие, новая дата события уже наступила");
            throw new RequestError(HttpStatus.BAD_REQUEST, "Невозможно изменить событие, " +
                    "новая дата события уже наступила");
        }

        if ( oldEvent.getState() == State.PUBLISHED && eventAdminInDto.getStateAction() == ActionState.REJECT_EVENT ){
            log.info("событие можно отклонить, только если оно еще не опубликовано. Текущий статус {}",
                    oldEvent.getState());
            throw new RequestError(HttpStatus.CONFLICT, "Невозможно изменить статус");
        }

        Event resultEvent = setNewParameters(oldEvent, eventAdminInDto);
        resultEvent.setId(eventId);
        log.info("Событие {} изменено на {}", oldEvent, resultEvent);

        return eventService.saveChangeEventForAdmin(resultEvent);
    }

    @Override
    public Collection<EventOutDto> findEvents(List<Integer> users, List<State> states, List<Integer> categories,
                                              String rangeStart, String rangeEnd, Integer from, Integer size) {
        LocalDateTime startFormat = null;
        LocalDateTime endFormat = null;

        if (rangeStart != null) {
            startFormat = LocalDateTime.parse(rangeStart, dateTimeFormatter);
        }
        if (rangeEnd != null) {
            endFormat = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        }
        log.info("Запрос на получение списка событий с заданными параметрами" +
                " администратором ");

        return eventService.findEventsWithParameters(users, states, categories,
                startFormat, endFormat, from, size);
    }

    @Override
    public CompOutDto createNewCompilation(CompInDto compInDto) {
        log.info("Создание новой подборки {}", compInDto);
        Collection<Event> events;
        if(compInDto.getEvents() != null)
            events = eventService.getEventListByEventIds(compInDto.getEvents());
        else
            events = new ArrayList<>();
        Compilation compilation = CompMapper.toCompilation(compInDto, events);
        Compilation compilationResult = compRepository.save(compilation);

        return CompMapper.toCompilationDto(compilationResult,
                eventService.getEventShortListWithSort(compilationResult.getEvents(), false));
    }

    @Override
    public CompOutDto updateCompilation(Integer compId, AdmCompInDto compilationRequest) {
        log.info("Изменение подборки с id {} администратором", compId);
        return compService.updateCompilation(compId, compilationRequest);
    }

    @Override
    public void deleteUser(Integer userId) {
        log.info("Запрос на удаление администратором пользователя {}", userId);
        userService.deleteUser(userId);
    }

    @Override
    public void deleteCategory(Integer catId) {
        log.info("Запрос на удалении администратором категории {}", catId);
        categoryService.deleteById(catId);
    }

    @Override
    public void deleteCompilation(Integer compId) {
        log.info("Запрос на удаление администратором подборки {}", compId);
        compService.deleteCompilation(compId);
    }

    private Event setNewParameters(Event event, EventAdminInDto updateEvent) {
        if (updateEvent.getAnnotation() == null && updateEvent.getDescription() == null &&
                updateEvent.getParticipantLimit() == null && updateEvent.getLocation() == null &&
                updateEvent.getTitle() == null) {
            if (updateEvent.getStateAction().equals(ActionState.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            } else {
                event.setState(State.CANCELED);
            }
            return event;
        }

        return updateFullEventParamsByAdmin(event, updateEvent);
    }

    private Event updateFullEventParamsByAdmin(Event event, EventAdminInDto updateEvent) {
        if (event.getCategory() != null && !Objects.equals(event.getCategory().getId(),
                updateEvent.getCategory())) {
            Category newCategory = CategoryMapper.toCategory(categoryService
                    .getById(updateEvent.getCategory()));
            event.setCategory(newCategory);
        }
        if (event.getLocation() != null && !Objects.equals(event.getLocation().getLat(),
                updateEvent.getLocation().getLat()) ||
                !Objects.equals(event.getLocation().getLon(), updateEvent.getLocation().getLon())) {
            locationService.deleteLocation(event.getLocation());
            Location newLocation = locationService.saveLocation(updateEvent.getLocation());
            event.setLocation(newLocation);
        }
        NewEventInDto eventDto = EventMapper.toNewEventInDto(updateEvent);
        Event resultEvent = EventMapper.toEvent(event.getInitiator(), event.getCategory(),
                event.getLocation(), eventDto);
        if (updateEvent.getStateAction().equals(ActionState.PUBLISH_EVENT)) {
            resultEvent.setState(State.PUBLISHED);
            resultEvent.setPublishedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        } else {
            resultEvent.setState(State.CANCELED);
        }
        return resultEvent;
    }
}
