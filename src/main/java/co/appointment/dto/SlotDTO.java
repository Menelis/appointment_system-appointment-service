package co.appointment.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SlotDTO {
    private Integer id;

    @Column(nullable = false, length = 20, name = "slot_start")
    private String slotStart;

    @Column(nullable = false, length = 20, name = "slot_end")
    private String slotEnd;
}
