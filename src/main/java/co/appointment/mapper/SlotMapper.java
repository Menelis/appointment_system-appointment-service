package co.appointment.mapper;

import co.appointment.entity.Slot;
import co.appointment.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlotMapper {

    private final SlotRepository slotRepository;

    public Slot fromId(final Integer id) {
        if(id == null) {
            return null;
        }
        return slotRepository.findById(id).orElse(null);
    }
}
