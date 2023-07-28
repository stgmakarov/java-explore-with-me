package ru.practicum.compilation.service;

import ru.practicum.admin.dto.AdmCompInDto;
import ru.practicum.compilation.dto.CompOutDto;

import java.util.Collection;

public interface CompService {
    CompOutDto getCompilationById(Integer compId);

    Collection<CompOutDto> getCompilationWithParam(boolean pinned, Integer from, Integer size);

    CompOutDto updateCompilation(Integer compId, AdmCompInDto compilationRequest);

    void deleteCompilation(Integer compId);
}
