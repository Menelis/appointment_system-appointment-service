package co.appointment.dto;

import jakarta.persistence.Column;
import java.util.UUID;
import lombok.Data;

@Data
public class SlotDTO {
    private UUID id;

    @Column(nullable = false, length = 20, name = "slot_start")
    private String slotStart;

    @Column(nullable = false, length = 20, name = "slot_end")
    private String slotEnd;
}
