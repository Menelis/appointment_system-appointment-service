package co.appointment.dto;

import co.appointment.record.BranchRecord;
import co.appointment.record.UserRecord;
import lombok.Data;

import java.util.Date;

@Data
public class AppointmentDTO {
    private Long id;
    private UserRecord user;
    private BranchRecord branch;
    private Date appointmentDate;
    private SlotDTO slot;
    private String description;
    private String referenceNo;
    private String status;
}
