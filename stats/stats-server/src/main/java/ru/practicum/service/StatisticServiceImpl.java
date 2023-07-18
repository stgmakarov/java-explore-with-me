package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatisticInDto;
import ru.practicum.dto.StatisticOutDto;
import ru.practicum.exception.RequestError;
import ru.practicum.mapper.StatisticMapper;
import ru.practicum.model.AppsModel;
import ru.practicum.model.StatisticModel;
import ru.practicum.repository.AppsRepository;
import ru.practicum.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;
    private final AppsRepository appsRepository;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Сохранение статистики
     *
     * @param statisticInDto
     */
    @Override
    public void saveStatistics(StatisticInDto statisticInDto) {
        AppsModel appsModel = getOrCreateApp(statisticInDto);
        log.info("Statistic save {} from ip {}",
                statisticInDto.getUri(),
                statisticInDto.getIp());
        StatisticModel statisticModel = StatisticMapper.toStaticModel(statisticInDto, appsModel);
        statisticRepository.save(statisticModel);
    }

    /**
     * Чтение статситики
     *
     * @param start
     * @param end
     * @param uris
     * @param unique
     * @return
     */
    @Override
    public Collection<StatisticOutDto> getStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startFormat = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endFormat = LocalDateTime.parse(end, dateTimeFormatter);
        checkPeriod(startFormat, endFormat);
        Collection<StatisticOutDto> resultCollection;
        if (uris != null){
            if (unique) {
                log.info("getStatsWithUniqIp {}", uris);
                resultCollection = statisticRepository.getStatsUniqIp(startFormat, endFormat, uris);
            } else {
                log.info("getStatsWithNotUniqIp {}", uris);
                resultCollection = statisticRepository.getStatsWOUniqIp(startFormat, endFormat, uris);
            }
        } else {
            if (unique) {
                log.info("getStatsWithUniqIp {}", uris);
                resultCollection = statisticRepository.getStatsWOUriUniqIp(startFormat, endFormat);
            } else {
                log.info("getStatsWithNotUniqIp {}", uris);
                resultCollection = statisticRepository.getStatsWOUriWOUniqIp(startFormat, endFormat);
            }
        }

        return resultCollection;
    }

    private AppsModel getOrCreateApp(StatisticInDto statisticInDto) {
        return appsRepository.getAppByName(statisticInDto.getApp())
                .orElseGet(() -> appsRepository.save(StatisticMapper.toApp(statisticInDto)));
    }

    /**
     * Проверка корректности периода
     *
     * @param start
     * @param end
     */
    private void checkPeriod(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || start.isAfter(LocalDateTime.now())) {
            log.info("Wrong period: Start {} - End {} ", start, end);
            throw new RequestError(HttpStatus.BAD_REQUEST, "Wrong period");
        }
    }
}
