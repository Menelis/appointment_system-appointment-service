package co.appointment.service;

import co.appointment.dto.SlotDTO;
import co.appointment.mapper.SlotToDTOMapper;
import co.appointment.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;
    private final SlotToDTOMapper slotToDTOMapper;

    public List<SlotDTO> getAllSlot() {
        return slotRepository.findAll()
                .stream()
                .map(slotToDTOMapper::toDTO)
                .toList();
    }
}
