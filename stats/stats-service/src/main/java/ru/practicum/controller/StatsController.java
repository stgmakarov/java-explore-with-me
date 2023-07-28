package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatisticInDto;
import ru.practicum.dto.StatisticOutDto;
import ru.practicum.service.StatisticService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
public class StatsController {
    @Autowired
    private StatisticService statisticService;

    /**Сохранение статистики
     * @param statisticInDto
     */
    @PostMapping(path = "/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void saveStatistics(@RequestBody @Valid StatisticInDto statisticInDto) {
        statisticService.saveStatistics(statisticInDto);
    }

    /**Чтение статситики
     * @param start     с
     * @param end       по
     * @param uris      URI
     * @param unique    флаг уникальности
     * @return
     */
    @GetMapping(path = "/stats")
    public Collection<StatisticOutDto> getStats(@RequestParam(value = "start") String start,
                                                @RequestParam(value = "end") String end,
                                                @RequestParam(value = "uris", required = false) List<String> uris,
                                                @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        return statisticService.getStats(start, end, uris, unique);
    }
}
