package co.appointment.dto;

import co.appointment.shared.record.BranchSummary;
import co.appointment.shared.record.UserSummary;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

@Data
public class AppointmentDTO {
    private UUID id;
    private UserSummary userSummary;
    private BranchSummary branchSummary;
    private LocalDate appointmentDate;
    private SlotDTO slot;
    private String description;
    private String referenceNo;
    private String status;
}
