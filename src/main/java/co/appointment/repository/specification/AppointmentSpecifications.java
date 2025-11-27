package co.appointment.repository.specification;

import co.appointment.entity.Appointment;
import org.springframework.data.jpa.domain.Specification;

public class AppointmentSpecifications {
    public static Specification<Appointment> notEqualToStatus(final String status) {
        return (root, query, builder) -> builder.notEqual(root.get("status"), status);
    }
}
