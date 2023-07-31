package ru.practicum.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.dto.AdmCompInDto;
import ru.practicum.compilation.dto.CompOutDto;
import ru.practicum.compilation.mapper.CompMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompRepository;
import ru.practicum.error.RequestError;
import ru.practicum.event.service.EventService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class CompServiceImpl implements CompService {
    @Autowired
    private CompRepository compRepository;
    @Autowired
    private EventService eventService;

    @Override
    public CompOutDto getCompilationById(Integer compId) {
        log.info("Запрос на получение подборки с id = {}", compId);
        Compilation compilation = compRepository.findById(compId).orElse(null);
        if (compilation == null) {
            log.info("Подборки с id {} не найдено", compId);
            throw new RequestError(HttpStatus.NOT_FOUND,
                    "Подборки под id " + compId + " не найдено");
        }
        return CompMapper.toCompilationDto(compilation,
                eventService.getEventShortListWithSort(compilation.getEvents(), false));
    }

    @Override
    public Collection<CompOutDto> getCompilationWithParam(boolean pinned, Integer from, Integer size) {
        log.info("Запрос на получение подборки, где pinned - {}", pinned);
        List<CompOutDto> result = new ArrayList<>();
        from = from / size;
        Page<Compilation> compilations = compRepository
                .getCompilationsByPinned(pinned, PageRequest.of(from, size));
        compilations.forEach(compilation -> result.add(CompMapper.toCompilationDto(compilation,
                eventService.getEventShortListWithSort(compilation.getEvents(), false))));
        return result;
    }

    @Override
    @Transactional
    public CompOutDto updateCompilation(Integer compId, AdmCompInDto compilationRequest) {
        Compilation compilation = compRepository.findById(compId).orElse(null);
        if (compilation == null) {
            log.info("Подборки с id {} не найдено", compId);
            throw new RequestError(HttpStatus.NOT_FOUND,
                    "Подборки под id " + compId + " не найдено");
        }
        if (!isNullOrEmpty(compilationRequest.getEvents())) {
            log.info("Администратор изменил события в подборке");
            compilation.setEvents(eventService.getEventListByEventIds(compilationRequest.getEvents()));
        }
        if (compilationRequest.getTitle() != null) {
            log.info("Администратор изменил заголовок подборки");
            compilation.setTitle(compilationRequest.getTitle());
        }
        if (compilationRequest.getPinned() != null) {
            log.info("Администратор закрепил/открепил событие");
            compilation.setPinned(compilationRequest.getPinned());
        }
        Compilation compilationResult = compRepository.save(compilation);
        log.info("Изменения в подборке сохранены");
        return CompMapper.toCompilationDto(compilationResult,
                eventService.getEventShortListWithSort(compilationResult.getEvents(), false));
    }

    private boolean isNullOrEmpty(List<Integer> list) {
        if (list == null) return true;
        return list.isEmpty();
    }

    @Override
    @Transactional
    public void deleteCompilation(Integer compId) {
        Compilation compilation = compRepository.findById(compId).orElse(null);
        if (compilation == null) {
            log.info("Подборки с id {} не найдено", compId);
            throw new RequestError(HttpStatus.NOT_FOUND,
                    "Подборки под id " + compId + " не найдено");
        }
        log.info("Удаление подборки {}", compilation);
        compRepository.deleteById(compId);
    }
}
