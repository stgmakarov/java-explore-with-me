package ru.practicum.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.CategoryOutDto;
import ru.practicum.category.dto.CategoryInDto;
import ru.practicum.category.model.Category;

@UtilityClass
public class CategoryMapper {

    public static CategoryOutDto toCategoryOutDto(Category category) {
        CategoryOutDto categoryOutDto = new CategoryOutDto();
        categoryOutDto.setId(category.getId());
        categoryOutDto.setName(category.getName());
        return categoryOutDto;
    }

    public static Category toCategory(CategoryInDto categoryInDto) {
        Category category = new Category();
        category.setName(categoryInDto.getName());
        return category;
    }

    public static Category toCategory(CategoryOutDto categoryOutDto) {
        Category category = new Category();
        category.setId(categoryOutDto.getId());
        category.setName(categoryOutDto.getName());
        return category;
    }
}
