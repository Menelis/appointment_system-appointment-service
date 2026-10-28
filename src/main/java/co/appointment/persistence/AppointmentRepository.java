package co.appointment.persistence;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface AppointmentRepository
        extends CrudRepository<AppointmentEntity, UUID>, ListPagingAndSortingRepository<AppointmentEntity, UUID> {

    Page<AppointmentEntity> findAllByCustomerId(UUID customerId, Pageable pageable);

    Page<AppointmentEntity> findAllByReferenceNo(String referenceNo, Pageable pageable);

    @Query("""
            SELECT (COUNT(*) > 0)::boolean as Result
            FROM appointments
            WHERE customer_id = :customerId
                  AND appointment_date = :appointmentDate
                  AND upper(status) <> upper(:status)
""")
    boolean activeCustomerAppointmentExists(UUID customerId, LocalDate appointmentDate, String status);
}
