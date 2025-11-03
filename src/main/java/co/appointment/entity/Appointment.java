package co.appointment.entity;

import co.appointment.constant.AppConstants;
import co.appointment.shared.entity.base.BaseEntity;
import co.appointment.util.ObjectUtils;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
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

    @Column(nullable = false, name = "appointment_status", length = 20)
    private String status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;
}
