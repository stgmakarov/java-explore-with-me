package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryOutDto;
import ru.practicum.category.dto.CategoryInDto;

import java.util.Collection;

public interface CategoryService {
    CategoryOutDto create(CategoryInDto categoryInDto);

    Collection<CategoryOutDto> getAll(Integer from, Integer size);

    CategoryOutDto getById(Integer catId);

    void deleteById(Integer catId);

    CategoryOutDto update(Integer catId, CategoryInDto categoryInDto);
}
