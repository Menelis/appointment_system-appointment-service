package co.appointment.kafka;

import co.appointment.shared.infastructure.AbstractKafkaPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService extends AbstractKafkaPublisher {

    public KafkaProducerService(final KafkaTemplate<String, byte[]> kafkaTemplate, final ObjectMapper objectMapper) {
        super(kafkaTemplate, objectMapper);
    }
}
