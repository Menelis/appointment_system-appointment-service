package co.appointment.mapper;

import co.appointment.shared.record.UserRecord;
import co.appointment.shared.service.GrcpAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final GrcpAuthService grcpAuthService;

    public UserRecord fromId(final UUID id) {
        if(id == null) {
            return null;
        }
        return grcpAuthService.getUserById(id);
    }
}
