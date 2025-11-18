package co.appointment.service.grcp;


import co.appointment.grpc.AuthServiceGrpc;
import co.appointment.shared.service.GrcpAuthService;
import co.appointment.shared.service.GrcpAuthServiceBase;


public class GrcpAuthServiceImpl extends GrcpAuthServiceBase implements GrcpAuthService {

    public GrcpAuthServiceImpl(final AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub) {
        super(authServiceBlockingStub);
    }
}
