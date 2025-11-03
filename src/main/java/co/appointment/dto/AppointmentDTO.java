package co.appointment.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class AppointmentDTO {
    private Long id;
    private UUID userId;
    private Integer branchId;
    private Date appointmentDate;
    private SlotDTO slot;
    private String description;
    private String referenceNo;
    private String status;
}
