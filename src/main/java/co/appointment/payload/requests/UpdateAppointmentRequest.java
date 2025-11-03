package co.appointment.payload.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateAppointmentRequest extends NewAppointmentRequest {

    @NotEmpty(message = "Appointment Id cannot not be empty")
    @NotBlank(message = "Appointment Id cannot be blank")
    @NotNull(message = "Appointment Id cannot be null")
    private Long id;

    private String status;
}
