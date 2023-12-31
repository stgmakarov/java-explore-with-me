package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.common.GlobalConsts;
import ru.practicum.dto.StatisticInDto;
import ru.practicum.dto.StatisticOutDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final DateTimeFormatter dateTimeFormatter = GlobalConsts.getDateTimeFormatter();
    @Value("${statistic-server}")
    private String statServUrl;

    public void save(StatisticInDto statisticInDto) {
        log.info("Save req {}", statisticInDto);
        restTemplate.postForLocation(statServUrl + "/hit", statisticInDto);
    }

    public List<StatisticOutDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        String startFormat = start.format(dateTimeFormatter);
        String endFormat = end.format(dateTimeFormatter);
        log.info("Get req from {} to {}, uniq flag {}", startFormat, endFormat, unique);

        ResponseEntity<StatisticOutDto[]> list = restTemplate.getForEntity(statServUrl + "/stats?start=" + startFormat +
                        "&end=" + endFormat + "&uris=" + uris + "&unique=" + unique,
                StatisticOutDto[].class);
        return Arrays.asList(Objects.requireNonNull(list.getBody()));
    }
}
