package co.appointment.repository.specification;

import co.appointment.entity.Appointment;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class AppointmentSpecifications {
    public static Specification<Appointment> notEqualToStatus(final String status) {
        return (root, query, builder) -> builder.notEqual(root.get("status"), status);
    }
    public static Specification<Appointment> referenceNoContains(final String searchTerm) {
        return (root, query, builder) -> builder.like(root.get("referenceNo"), "%" + searchTerm + "%");
    }
    public static Specification<Appointment> equalsCustomerId(final UUID customerId) {
        return (root, query, builder) -> builder.equal(root.get("customerId"), customerId);
    }
}
