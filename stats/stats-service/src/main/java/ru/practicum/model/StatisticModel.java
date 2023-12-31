package ru.practicum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.dto.StatisticOutDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "statistics", schema = "public")
@NamedNativeQuery(name = "FindStatsWOUniqueIp",
        query = "select a.name as app,  s.uri as uri, count(s.ip) as hits from statistics s " +
                "join apps a " +
                "on s.app_id = a.id " +
                "where s.timestamp > :start " +
                "and  s.timestamp < :end " +
                "and (s.uri in :uris) " +
                "group by s.uri, a.name order by hits desc", resultSetMapping = "StatsOutDto")

@NamedNativeQuery(name = "FindStatsUniqueIp",
        query = "select a.name as app,  s.uri as uri, count(distinct s.ip) as hits from statistics s " +
                "join apps a " +
                "on s.app_id = a.id " +
                "where s.timestamp > :start " +
                "and  s.timestamp < :end " +
                "and (s.uri in :uris) " +
                "group by s.uri, a.name order by hits desc", resultSetMapping = "StatsOutDto")

@NamedNativeQuery(name = "FindStatsWOUriWOUniqueIp",
        query = "select a.name as app,  s.uri as uri, count(s.ip) as hits from statistics s " +
                "join apps a " +
                "on s.app_id = a.id " +
                "where s.timestamp > :start " +
                "and  s.timestamp < :end " +
                "group by s.uri, a.name order by hits desc", resultSetMapping = "StatsOutDto")

@NamedNativeQuery(name = "FindStatsWOUriUniqueIp",
        query = "select a.name as app,  s.uri as uri, count(distinct s.ip) as hits from statistics s " +
                "join apps a " +
                "on s.app_id = a.id " +
                "where s.timestamp > :start " +
                "and  s.timestamp < :end " +
                "group by s.uri, a.name order by hits desc", resultSetMapping = "StatsOutDto")

@SqlResultSetMapping(name = "StatsOutDto", classes = {
        @ConstructorResult(
                columns = {
                        @ColumnResult(name = "app", type = String.class),
                        @ColumnResult(name = "uri", type = String.class),
                        @ColumnResult(name = "hits", type = Integer.class),
                },
                targetClass = StatisticOutDto.class
        )
})
public class StatisticModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "app_id")
    private AppsModel appsModel;
    @Column(name = "uri")
    private String uri;
    @Column(name = "ip")
    private String ip;
    @DateTimeFormat
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
