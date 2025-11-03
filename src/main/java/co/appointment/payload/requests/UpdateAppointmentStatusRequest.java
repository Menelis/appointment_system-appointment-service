package co.appointment.payload.requests;

import lombok.Data;

@Data
public class UpdateAppointmentStatusRequest {
    private Long id;
    private String status;
}
