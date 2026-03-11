package ru.yandex.practicum.smarthome.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "measurements")
@Data
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private Instant timestamp;

    public Measurement() {}

    public Measurement(String deviceId, Double value, String unit, Instant timestamp) {
        this.deviceId = deviceId;
        this.value = value;
        this.unit = unit;
        this.timestamp = timestamp;
    }
}
