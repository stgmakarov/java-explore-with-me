package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryOutDto;
import ru.practicum.category.dto.CategoryInDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.error.RequestError;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryOutDto create(CategoryInDto categoryInDto) {
        log.info("Добавление новой категории {}", categoryInDto);
        Category category = categoryRepository.save(CategoryMapper.toCategory(categoryInDto));
        return CategoryMapper.toCategoryOutDto(category);
    }

    @Override
    public Collection<CategoryOutDto> getAll(Integer from, Integer size) {
        log.info("Запрошен список категорий");
        from = from / size;
        Collection<CategoryOutDto> result = new ArrayList<>();
        categoryRepository.getAllWithPagination(PageRequest.of(from, size)).forEach(category ->
                result.add(CategoryMapper.toCategoryOutDto(category)));
        return result;
    }

    @Override
    public CategoryOutDto getById(Integer catId) {
        Category category = checkContainCategory(catId);
        log.info("Запрошена категория {}", category);
        return CategoryMapper.toCategoryOutDto(category);
    }

    @Override
    public void deleteById(Integer catId) {
        checkContainCategory(catId);
        log.info("Удаление категории {}", catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryOutDto update(Integer catId, CategoryInDto categoryInDto) {
        Category category = checkContainCategory(catId);
        log.info("Изменение категории {} на категорию {}", catId, categoryInDto);
        category.setName(categoryInDto.getName());
        return CategoryMapper.toCategoryOutDto(categoryRepository.save(category));
    }

    private Category checkContainCategory(Integer catId) {
        Category category = categoryRepository.findById(catId).orElse(null);
        if (category == null) {
            throw new RequestError(HttpStatus.NOT_FOUND, "Категории под id " + catId + " не найдено");
        }
        return category;
    }
}
