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

    @GrpcClient("auth-service")
    AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    @Bean
    public GrcpAuthService grcpAuthService() {
        return new GrcpAuthServiceImpl(authServiceBlockingStub);
    }

    @GrpcClient("branch-service")
    BranchServiceGrpc.BranchServiceBlockingStub branchServiceBlockingStub;

    @Bean
    public GrcpBranchService grcpBranchService() {
        return new GrcpBranchServiceImpl(branchServiceBlockingStub);
    }
}
