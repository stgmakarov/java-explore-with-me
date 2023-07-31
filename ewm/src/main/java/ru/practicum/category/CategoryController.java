package ru.practicum.category;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryOutDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryOutDto> getCategories(@PositiveOrZero
                                                    @RequestParam(value = "from", defaultValue = "0")
                                                    Integer from,
                                                    @Positive
                                                    @RequestParam(value = "size", defaultValue = "10")
                                                    Integer size) {
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryOutDto getById(@PathVariable Integer catId) {
        return categoryService.getById(catId);
    }
}
