package co.appointment.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

@Data
public class AppointmentDTO {
    private UUID id;
    private UUID userId;
    // private UserRecord user;
    private UUID branchId;
    // private BranchRecord branch;
    private LocalDate appointmentDate;
    private UUID slotId;
    // private SlotDTO slot;
    private String description;
    private String referenceNo;
    private String status;
}
