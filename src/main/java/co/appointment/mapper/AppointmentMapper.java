package co.appointment.mapper;

import co.appointment.dto.AppointmentDTO;
import co.appointment.payload.requests.NewAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentRequest;
import co.appointment.persistence.AppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {MapperHelper.class})
public interface AppointmentMapper {

    // @Mapping(source = "customerId", target = "user")
    // @Mapping(source = "branchId", target = "branch")
    AppointmentDTO toDTO(AppointmentEntity appointment);

    // @Mapping(target = "slot", source = "slotId")
    AppointmentEntity toEntity(NewAppointmentRequest request);

    // @Mapping(target = "slot", source = "slotId")
    AppointmentEntity toEntity(UpdateAppointmentRequest request);
}
