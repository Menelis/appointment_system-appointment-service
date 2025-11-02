package co.appointment.entity;

import co.appointment.shared.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "appointments")
@Entity
public class Appointment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, name = "branch_id")
    private Integer branchId;

    @Column(nullable = false, name = "appointment_date")
    private Date appointmentDate;

    @Column(nullable = false, name = "slot_id")
    private Integer slotId;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, name = "reference_no")
    private String referenceNo;

    @Column(nullable = false, name = "appointment_status", length = 20)
    private String status;
}
