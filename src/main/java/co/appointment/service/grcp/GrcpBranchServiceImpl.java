package co.appointment.service.grcp;

import co.appointment.grpc.BranchServiceGrpc;
import co.appointment.shared.service.GrcpBranchService;
import co.appointment.shared.service.GrcpBranchServiceBase;

public class GrcpBranchServiceImpl extends GrcpBranchServiceBase implements GrcpBranchService {

    public GrcpBranchServiceImpl(BranchServiceGrpc.BranchServiceBlockingStub branchServiceBlockingStub) {
        super(branchServiceBlockingStub);
    }
}
