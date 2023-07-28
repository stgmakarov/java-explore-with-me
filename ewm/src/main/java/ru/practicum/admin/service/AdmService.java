package ru.practicum.admin.service;

import ru.practicum.admin.dto.AdmCompInDto;
import ru.practicum.admin.dto.EventAdminInDto;
import ru.practicum.category.dto.CategoryInDto;
import ru.practicum.category.dto.CategoryOutDto;
import ru.practicum.compilation.dto.CompInDto;
import ru.practicum.compilation.dto.CompOutDto;
import ru.practicum.event.dto.EventOutDto;
import ru.practicum.state.State;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserInDto;

import java.util.Collection;
import java.util.List;

public interface AdmService {

    CategoryOutDto createNewCategory(CategoryInDto categoryInDto);

    CategoryOutDto changeCategory(Integer catId, CategoryInDto categoryInDto);

    UserDto createUser(UserInDto userInDto);

    Collection<UserDto> getAllUsers(List<Integer> ids, Integer from, Integer size);

    EventOutDto changeEvent(Integer eventId, EventAdminInDto eventAdminInDto);

    Collection<EventOutDto> findEvents(List<Integer> users, List<State> states, List<Integer> categories,
                                       String rangeStart, String rangeEnd, Integer from, Integer size);

    CompOutDto createNewCompilation(CompInDto compInDto);

    CompOutDto updateCompilation(Integer compId, AdmCompInDto compilationRequest);

    void deleteUser(Integer userId);

    void deleteCategory(Integer catId);

    void deleteCompilation(Integer compId);

}
