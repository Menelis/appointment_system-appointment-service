package co.appointment.mapper;

import co.appointment.persistence.SlotEntity;
import co.appointment.persistence.SlotRepository;
import co.appointment.shared.record.BranchSummary;
import co.appointment.shared.record.UserRecord;
import co.appointment.shared.service.GrcpAuthService;
import co.appointment.shared.service.GrcpBranchService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperHelper {
    private final SlotRepository slotRepository;
    private final GrcpBranchService grcpBranchService;
    private final GrcpAuthService grcpAuthService;

    public SlotEntity mapSlotById(final UUID id) {
        if (id == null) {
            return null;
        }
        return slotRepository.findById(id).orElse(null);
    }

    public BranchSummary branchSummaryById(final UUID id) {
        if (id == null) {
            return null;
        }
        return grcpBranchService.getBranchById(id);
    }

    public UserRecord userInfoById(final UUID id) {
        if (id == null) {
            return null;
        }
        return grcpAuthService.getUserById(id);
    }
}
