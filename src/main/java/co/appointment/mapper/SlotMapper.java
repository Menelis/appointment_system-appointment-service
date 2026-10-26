package co.appointment.mapper;

import co.appointment.dto.SlotDTO;
import co.appointment.persistence.SlotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SlotMapper {
    SlotDTO toDTO(SlotEntity slot);
}
