package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.GlobalConsts;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;
    private final AppsRepository appsRepository;
    private final DateTimeFormatter dateTimeFormatter = GlobalConsts.getDateTimeFormatter();

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
    @Transactional
    public Collection<StatisticOutDto> getStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startFormat = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endFormat = LocalDateTime.parse(end, dateTimeFormatter);
        checkPeriod(startFormat, endFormat);
        Collection<StatisticOutDto> resultCollection;
        if (uris != null) {
            List<String> nUris = uris.stream()
                    .map(s -> s.replace("[","").replace("]",""))
                    .collect(Collectors.toList());
            if (unique) {
                log.info("getStatsWithUniqIp {}", nUris);
                resultCollection = statisticRepository.getStatsUniqIp(startFormat, endFormat, nUris);
            } else {
                log.info("getStatsWithNotUniqIp {}", nUris);
                resultCollection = statisticRepository.getStatsWOUniqIp(startFormat, endFormat, nUris);
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

    @Transactional
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
