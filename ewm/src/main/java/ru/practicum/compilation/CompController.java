package ru.practicum.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompOutDto;
import ru.practicum.compilation.service.CompService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/compilations")
public class CompController {
    @Autowired
    private CompService compService;

    @GetMapping()
    public Collection<CompOutDto> getCompilationWithParam(@RequestParam(value = "pinned", defaultValue = "false")
                                                              boolean pinned,
                                                          @PositiveOrZero
                                                              @RequestParam(value = "from", defaultValue = "0")
                                                              Integer from,
                                                          @Positive
                                                              @RequestParam(value = "size", defaultValue = "10")
                                                              Integer size) {

        return compService.getCompilationWithParam(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompOutDto getCompilationById(@PathVariable Integer compId) {
        return compService.getCompilationById(compId);
    }
}
