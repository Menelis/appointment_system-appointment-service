package co.appointment.mapper;

import co.appointment.dto.AppointmentDTO;
import co.appointment.payload.requests.NewAppointmentRequest;
import co.appointment.payload.requests.UpdateAppointmentRequest;
import co.appointment.persistence.AppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {MapperHelper.class})
public interface AppointmentMapper {

    @Mapping(target = "userSummary", source = "customerId", qualifiedByName = "mapUserSummary")
    @Mapping(target = "branchSummary", source = "branchId", qualifiedByName = "mapBranchSummary")
    @Mapping(target = "slot", source = "slotId", qualifiedByName = "mapSlot")
    AppointmentDTO toDTO(AppointmentEntity appointment);

    AppointmentEntity toEntity(NewAppointmentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void toEntity(UpdateAppointmentRequest request, @MappingTarget AppointmentEntity appointment);
}
