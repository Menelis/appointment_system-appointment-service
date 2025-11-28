package co.appointment.mapper;


import co.appointment.shared.record.BranchRecord;
import co.appointment.shared.service.GrcpBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BranchMapper {
    private final GrcpBranchService grcpBranchService;

    public BranchRecord fromId(final Integer id) {
        if(id == null) {
            return null;
        }
        return grcpBranchService.getBranchById(id);
    }
}
