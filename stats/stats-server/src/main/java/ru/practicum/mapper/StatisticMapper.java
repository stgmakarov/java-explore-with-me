package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.StatisticInDto;
import ru.practicum.model.AppsModel;
import ru.practicum.model.StatisticModel;

@UtilityClass
public class StatisticMapper {

    public static StatisticModel toStaticModel(StatisticInDto statisticInDto, AppsModel appsModel) {
        StatisticModel statisticModel = new StatisticModel();
        statisticModel.setAppsModel(appsModel);
        statisticModel.setUri(statisticInDto.getUri());
        statisticModel.setIp(statisticInDto.getIp());
        statisticModel.setTimestamp(statisticInDto.getTimestamp());

        return statisticModel;
    }

    public static AppsModel toApp(StatisticInDto statisticInDto) {
        AppsModel appsModel = new AppsModel();
        appsModel.setName(statisticInDto.getApp());
        return appsModel;
    }
}
