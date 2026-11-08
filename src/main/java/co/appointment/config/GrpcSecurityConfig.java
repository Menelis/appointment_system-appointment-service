package co.appointment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.grpc.server.security.AuthenticationProcessInterceptor;
import org.springframework.grpc.server.security.GrpcSecurity;

@Configuration
public class GrpcSecurityConfig {

    @Bean
    @GlobalServerInterceptor
    public AuthenticationProcessInterceptor grpcSecurityFilterChain(final GrpcSecurity grpcSecurity) throws Exception {
        return grpcSecurity
                .authorizeRequests(requests -> requests.allRequests().permitAll())
                .build();
    }
}
