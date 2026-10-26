package co.appointment.payload.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

@Data
public class NewAppointmentRequest {

    private UUID branchId;

    @NotNull(message = "Appointment Date cannot be empty")
    private LocalDate appointmentDate;

    private UUID slotId;

    @Size(max = 500, message = "Only 500 characters are allowed for appointment description")
    private String description;
}
