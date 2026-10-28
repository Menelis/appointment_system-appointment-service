package co.appointment.payload.requests;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentStatusRequest {
    private UUID id;
    private String status;
    private String reason;
}
