package ru.practicum.location.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Override
    @Transactional
    public Location saveLocation(LocationDto locationDto) {
        log.info("Сохранение локации {}", locationDto);
        return locationRepository.save(LocationMapper.toLocation(locationDto));
    }

    @Override
    @Transactional
    public void deleteLocation(Location location) {
        log.info("Удаление локации {}", location);
        locationRepository.delete(location);
    }

}
