package ru.practicum.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.dto.CompOutDto;
import ru.practicum.compilation.dto.CompInDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortOutDto;
import ru.practicum.event.model.Event;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@UtilityClass
public class CompMapper {

    public static Compilation toCompilation(CompInDto compilationDto, Collection<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setTitle(compilationDto.getTitle());
        compilation.setPinned(compilationDto.isPinned());
        return compilation;
    }

    public static CompOutDto toCompilationDto(Compilation compilation, Collection<EventShortOutDto> eventShorts) {
        CompOutDto compOutDto = new CompOutDto();
        Collection<EventShortOutDto> events = eventShorts.stream().sorted(Comparator.comparing(EventShortOutDto::getViews)
                .reversed()).collect(Collectors.toList());
        compOutDto.setId(compilation.getId());
        compOutDto.setPinned(compilation.isPinned());
        compOutDto.setTitle(compilation.getTitle());
        compOutDto.setEvents(events);
        return compOutDto;
    }
}
