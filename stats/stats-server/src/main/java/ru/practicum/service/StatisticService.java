package ru.practicum.service;

import ru.practicum.dto.StatisticInDto;
import ru.practicum.dto.StatisticOutDto;

import java.util.Collection;
import java.util.List;

public interface StatisticService {

    void saveStatistics(StatisticInDto statisticInDto);

    Collection<StatisticOutDto> getStats(String start, String end, List<String> uris, boolean unique);
}
