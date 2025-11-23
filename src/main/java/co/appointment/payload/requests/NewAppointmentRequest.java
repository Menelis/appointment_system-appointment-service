package co.appointment.payload.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Date;

@Data
public class NewAppointmentRequest {

    private Integer branchId;

    @NotNull(message = "Appointment Date cannot be empty")
    private Date appointmentDate;

    private Integer slotId;

    @Size(max = 500, message = "Only 500 characters are allowed for appointment description")
    private String description;
}
