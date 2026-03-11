package ru.yandex.practicum.smarthome.dto;

import java.time.Instant;

public record TemperatureResponse(
        double value,
        String unit,
        Instant timestamp,
        String location,
        String status,
        String sensorId,
        String sensorType,
        String description
) {
}
