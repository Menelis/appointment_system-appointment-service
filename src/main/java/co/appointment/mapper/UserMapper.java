package co.appointment.mapper;

import co.appointment.grpc.GetUserResponse;
import co.appointment.record.UserRecord;
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
        GetUserResponse user = grcpAuthService.getUserById(id);
        if(user == null) {
            return null;
        }
        return new UserRecord(user.getFullName());
    }
}
