package co.appointment.kafka.event;

import co.appointment.shared.event.DomainEvent;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEvent implements DomainEvent {

    private String referenceNo;

    private UUID eventId;

    @Override
    public UUID getEventId() {
        if (eventId == null) {
            return UUID.randomUUID();
        }
        return eventId;
    }
}
