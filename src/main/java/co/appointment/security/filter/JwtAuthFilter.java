package co.appointment.security.filter;

import co.appointment.grpc.GetUserResponse;
import co.appointment.shared.constant.TokenConstants;
import co.appointment.shared.model.JwtSettings;
import co.appointment.shared.security.UserDetailsImpl;
import co.appointment.shared.service.GrcpAuthService;
import co.appointment.shared.util.SharedJwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtSettings jwtSettings;
    private final GrcpAuthService grcpAuthService;

    @Override
    protected void doFilterInternal(
            @NotNull final HttpServletRequest request,
            @NotNull final HttpServletResponse response,
            @NotNull final FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(TokenConstants.AUTHORIZATION_HEADER);

        if(!StringUtils.hasText(authHeader) || !authHeader.startsWith(TokenConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token = authHeader.substring(TokenConstants.TOKEN_PREFIX.length());
        final String emailAddress = SharedJwtUtils.extractClaimByKey(token, TokenConstants.EMAIL, jwtSettings.getSecret());
        if(!StringUtils.hasText(emailAddress)) {
            filterChain.doFilter(request, response);
        }
        log.info("Token is: {}", token);
        log.info("Email address is: {}", emailAddress);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            GetUserResponse userResponse = grcpAuthService.getUserByEmail(emailAddress);
            UserDetailsImpl userDetails = UserDetailsImpl.build(userResponse);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
