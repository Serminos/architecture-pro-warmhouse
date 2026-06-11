package ru.yandex.practicum.smarthome.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.smarthome.dto.TemperatureResponse;

import java.time.Instant;
import java.util.Random;

@RestController
public class TemperatureController {

    private final Random random = new Random();

    @GetMapping("/temperature")
    public TemperatureResponse getTemperatureByLocation(@RequestParam String location) {
        double value = 15 + random.nextDouble() * 15; // 15-30
        String sensorId = mapLocationToSensorId(location);
        return new TemperatureResponse(
                value,
                "Celsius",
                Instant.now(),
                location,
                "online",
                sensorId,
                "temperature",
                ""
        );
    }

    @GetMapping("/temperature/{sensorId}")
    public TemperatureResponse getTemperatureBySensorId(@PathVariable String sensorId) {
        double value = 15 + random.nextDouble() * 15;
        String location = mapSensorIdToLocation(sensorId);
        return new TemperatureResponse(
                value,
                "Celsius",
                Instant.now(),
                location,
                "online",
                sensorId,
                "temperature",
                ""
        );
    }

    private String mapLocationToSensorId(String location) {
        switch (location) {
            case "Living Room":
                return "1";
            case "Bedroom":
                return "2";
            case "Kitchen":
                return "3";
            default:
                return "0";
        }
    }

    private String mapSensorIdToLocation(String sensorId) {
        switch (sensorId) {
            case "1":
                return "Living Room";
            case "2":
                return "Bedroom";
            case "3":
                return "Kitchen";
            default:
                return "Unknown";
        }
    }
}
