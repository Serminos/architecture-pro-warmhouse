package ru.yandex.practicum.smarthome.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
public class TemperatureEvent {
    @JsonProperty("sensorId")
    private Long sensorId;
    private String location;
    private Double value;
    private String unit;
    private Instant timestamp;
}