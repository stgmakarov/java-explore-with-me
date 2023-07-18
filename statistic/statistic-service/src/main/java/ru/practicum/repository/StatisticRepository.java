package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatisticOutDto;
import ru.practicum.model.StatisticModel;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<StatisticModel, Integer> {

    @Query(nativeQuery = true, name = "FindStatsWOUniqueIp")
    Collection<StatisticOutDto> getStatsWOUniqIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(nativeQuery = true, name = "FindStatsUniqueIp")
    Collection<StatisticOutDto> getStatsUniqIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(nativeQuery = true, name = "FindStatsWOUriWOUniqueIp")
    Collection<StatisticOutDto> getStatsWOUriWOUniqIp(LocalDateTime start, LocalDateTime end);

    @Query(nativeQuery = true, name = "FindStatsWOUriUniqueIp")
    Collection<StatisticOutDto> getStatsWOUriUniqIp(LocalDateTime start, LocalDateTime end);
}
