package co.appointment.repository;

import co.appointment.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    Optional<Slot> findBySlotStartAndSlotEnd(String start, String end);
}
