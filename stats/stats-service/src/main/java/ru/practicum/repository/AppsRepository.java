package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.AppsModel;

import java.util.Optional;

public interface AppsRepository extends JpaRepository<AppsModel, Integer> {

    Optional<AppsModel> getAppByName(String name);
}
