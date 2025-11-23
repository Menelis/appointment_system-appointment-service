package co.appointment.mapper;

import co.appointment.dto.SlotDTO;
import co.appointment.entity.Slot;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SlotToDTOMapper {
    SlotDTO toDTO(Slot slot);
}
