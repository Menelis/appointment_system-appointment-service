package co.appointment.mapper;

import co.appointment.dto.AppointmentDTO;
import co.appointment.entity.Appointment;
import co.appointment.payload.requests.NewAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
uses = SlotMapper.class)
public interface AppointmentToDTOMapper {

    AppointmentDTO toDTO(Appointment appointment);

    @Mapping(target = "slot", source = "slotId")
    Appointment toEntity(NewAppointmentRequest request);

    @Mapping(target = "slot", source = "slotId")
    Appointment toEntity(UpdateAppointmentRequest request);
}
