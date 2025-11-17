package co.appointment.repository;

import co.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findAllByCustomerId(UUID customerId, Pageable pageable);
    Page<Appointment> findAllByReferenceNo(String referenceNo, Pageable pageable);
}
