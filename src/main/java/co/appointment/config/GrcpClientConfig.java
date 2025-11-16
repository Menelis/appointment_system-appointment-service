package co.appointment.config;

import co.appointment.grpc.AuthServiceGrpc;
import co.appointment.service.grcp.GrcpAuthServiceImpl;
import co.appointment.shared.service.GrcpAuthService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrcpClientConfig {

    @GrpcClient("user-service")
    AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    @Bean
    public GrcpAuthService grcpAuthService() {
        return new GrcpAuthServiceImpl(authServiceBlockingStub);
    }
}
