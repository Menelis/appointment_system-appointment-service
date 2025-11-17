package co.appointment.payload.requests;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Date;

@Data
public class NewAppointmentRequest {
    private Integer branchId;
    private Date appointmentDate;
    private Integer slotId;
    @Size(max = 500, message = "Only 500 characters are allowed for appointment description")
    private String description;
}
