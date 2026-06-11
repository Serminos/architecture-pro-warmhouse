package ru.yandex.practicum.smarthome.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.smarthome.model.Measurement;
import ru.yandex.practicum.smarthome.model.TemperatureEvent;
import ru.yandex.practicum.smarthome.repository.MeasurementRepository;

@Component
public class TemperatureEventConsumer {

    @Autowired
    private MeasurementRepository measurementRepository;

    @KafkaListener(topics = "${spring.kafka.topics.temperature}")
    public void consume(TemperatureEvent event) {
        if (event.getSensorId() == null || event.getValue() == null) {
            return;
        }
        Measurement measurement = new Measurement(
                String.valueOf(event.getSensorId()),
                event.getValue(),
                event.getUnit() != null ? event.getUnit() : "C",
                event.getTimestamp() != null ? event.getTimestamp() : java.time.Instant.now()
        );
        measurementRepository.save(measurement);
    }
}