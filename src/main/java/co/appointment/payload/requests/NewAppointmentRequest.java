package co.appointment.payload.requests;

import co.appointment.entity.Slot;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class NewAppointmentRequest {

    @NotEmpty(message = "Branch Id cannot not be empty")
    @NotBlank(message = "Branch Id cannot be blank")
    @NotNull(message = "Branch Id cannot be null")
    private Integer branchId;

    @NotEmpty(message = "Appointment Date cannot not be empty")
    @NotBlank(message = "Appointment Date cannot be blank")
    @NotNull(message = "Appointment Date cannot be null")
    private Date appointmentDate;

    @NotEmpty(message = "Slot Id cannot not be empty")
    @NotBlank(message = "Slot Id cannot be blank")
    @NotNull(message = "Slot Id cannot be null")
    private Integer slotId;

    @Size(max = 500, message = "Only 500 characters are allowed for appointment description")
    private String description;


}
