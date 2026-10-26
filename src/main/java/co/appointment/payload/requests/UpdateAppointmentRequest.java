package co.appointment.payload.requests;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateAppointmentRequest extends NewAppointmentRequest {

    @NotNull(message = "Appointment Id cannot be null")
    private UUID id;

    private String status;
}
