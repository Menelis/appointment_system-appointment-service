package co.appointment.entity;

import co.appointment.shared.entity.base.BaseEntity;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "slots")
public class Slot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20, name = "slot_start")
    private String slotStart;

    @Column(nullable = false, length = 20, name = "slot_end")
    private String slotEnd;

    public Slot() {}

    public Slot(String slotStart, String slotEnd) {
        this.slotStart = slotStart;
        this.slotEnd = slotEnd;
    }
}
