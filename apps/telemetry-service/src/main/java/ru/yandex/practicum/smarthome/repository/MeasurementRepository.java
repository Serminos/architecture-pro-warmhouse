package ru.yandex.practicum.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.smarthome.model.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
}