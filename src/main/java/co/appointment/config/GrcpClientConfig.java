package co.appointment.config;

import co.appointment.grpc.AuthServiceGrpc;
import co.appointment.grpc.BranchServiceGrpc;
import co.appointment.service.grcp.GrcpAuthServiceImpl;
import co.appointment.service.grcp.GrcpBranchServiceImpl;
import co.appointment.shared.service.GrcpAuthService;
import co.appointment.shared.service.GrcpBranchService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrcpClientConfig {

    @Bean
    public GrcpAuthService grcpAuthService(
            @GrpcClient("auth-service") final AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub) {
        return new GrcpAuthServiceImpl(authServiceBlockingStub);
    }

    @Bean
    public GrcpBranchService grcpBranchService(
            @GrpcClient("branch-service") final BranchServiceGrpc.BranchServiceBlockingStub branchServiceBlockingStub) {
        return new GrcpBranchServiceImpl(branchServiceBlockingStub);
    }
}
