package co.appointment.entity;

import co.appointment.shared.entity.base.BaseEntity;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "appointments")
@Entity
public class Appointment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(nullable = false, name = "branch_id")
    private Integer branchId;

    @Column(nullable = false, name = "appointment_date")
    private Date appointmentDate;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, name = "reference_no")
    private String referenceNo;

    @Column(nullable = false, name = "appointment_status", length = 50)
    private String status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;
}
