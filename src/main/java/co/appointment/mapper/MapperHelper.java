package co.appointment.mapper;

import co.appointment.dto.SlotDTO;
import co.appointment.persistence.SlotRepository;
import co.appointment.shared.record.BranchSummary;
import co.appointment.shared.record.UserSummary;
import co.appointment.shared.service.grcp.GrpcAuthClient;
import co.appointment.shared.service.grcp.GrpcBranchClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperHelper {

    private final SlotRepository slotRepository;
    private final GrpcAuthClient grpcAuthClient;
    private final GrpcBranchClient grpcBranchClient;

    @Named("mapSlot")
    public SlotDTO mapSlot(final UUID id) {
        if (id == null) {
            return null;
        }
        return slotRepository
                .findById(id)
                .map(slot -> SlotDTO.builder()
                        .id(slot.getId())
                        .slotStart(slot.getSlotStart())
                        .slotEnd(slot.getSlotEnd())
                        .build())
                .orElse(null);
    }

    @Named("mapBranchSummary")
    public BranchSummary mapBranchSummary(final UUID id) {
        if (id == null) {
            return null;
        }
        return grpcBranchClient.getBranchById(id);
    }

    @Named("mapUserSummary")
    public UserSummary mapUserSummary(final UUID id) {
        if (id == null) {
            return null;
        }
        return grpcAuthClient.getUserById(id);
    }
}
