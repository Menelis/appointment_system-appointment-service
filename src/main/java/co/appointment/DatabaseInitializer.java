package co.appointment;

import co.appointment.entity.Slot;
import co.appointment.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final SlotRepository slotRepository;

    @Override
    public void run(String... args) throws Exception {
        seedSlots();
    }
    void seedSlots() {
        List<Slot> slots = List.of(
                new Slot("08:00", "09:00"),
                new Slot("09:00", "10:00"),
                new Slot("10:00", "11:00"),
                new Slot("12:00", "13:00"),
                new Slot("13:00", "14:00"),
                new Slot("14:00", "15:00"),
                new Slot("15:00", "16:00"),
                new Slot("16:00", "17:00")
        );
        slots.forEach(slot -> {
            Optional<Slot> optionalSlot = slotRepository.findBySlotStartAndSlotEnd(slot.getSlotStart(), slot.getSlotEnd());
            if (optionalSlot.isEmpty()) {
                slotRepository.save(slot);
            }
        });
    }
}
