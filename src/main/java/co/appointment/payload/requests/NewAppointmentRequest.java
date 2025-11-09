package co.appointment.payload.requests;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NewAppointmentRequest {
    private Integer branchId;
    private LocalDate appointmentDate;
    private Integer slotId;
    @Size(max = 500, message = "Only 500 characters are allowed for appointment description")
    private String description;


}
