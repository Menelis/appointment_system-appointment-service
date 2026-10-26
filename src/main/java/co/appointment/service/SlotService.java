package co.appointment.service;

import co.appointment.dto.SlotDTO;
import co.appointment.mapper.SlotMapper;
import co.appointment.persistence.SlotRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;
    private final SlotMapper slotMapper;

    public List<SlotDTO> getAllSlot() {
        return slotRepository.findAll(Sort.by(Sort.Direction.ASC, "slotStart")).stream()
                .map(slotMapper::toDTO)
                .toList();
    }
}
