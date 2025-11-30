package co.appointment.config;

import co.appointment.shared.constant.RoleConstants;
import co.appointment.shared.util.SharedJwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Profile("!test")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http,
                                           final AppConfigProperties appConfigProperties) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(appConfigProperties.getWhiteList()).permitAll()
                        .requestMatchers(appConfigProperties.getAdminRoutes()).hasAnyRole(RoleConstants.ADMIN_ROLE, RoleConstants.USER_ROLE)
                        .anyRequest().authenticated())
                .oauth2ResourceServer((oath2) -> oath2.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(final AppConfigProperties appConfigProperties) {
        return SharedJwtUtils.convertJwtGrantedAuthorities(appConfigProperties.getAuthoritiesClaimName(),
                appConfigProperties.getAuthoritiesClaimPrefix());
    }
}
