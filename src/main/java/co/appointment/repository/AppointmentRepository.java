package co.appointment.repository;

import co.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    Page<Appointment> findAllByCustomerId(UUID customerId, Pageable pageable);
    Page<Appointment> findAllByReferenceNo(String referenceNo, Pageable pageable);
    @Query(
            nativeQuery = true, value = "SELECT (COUNT(*) > 0)::boolean as Result FROM appointments WHERE customer_id = :par_customer_id AND appointment_date = :par_appointment_date AND upper(appointment_status) <> upper(:par_status)")
    Boolean activeCustomerAppointmentExists(@Param("par_customer_id") UUID customerId,
                                @Param("par_appointment_date") Date appointmentDate,
                                @Param("par_status") String status);
}
