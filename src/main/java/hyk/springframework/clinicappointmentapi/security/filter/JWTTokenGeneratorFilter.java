package hyk.springframework.clinicappointmentapi.security.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
//@Component
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {
    private final Environment environment;

    public JWTTokenGeneratorFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {
            SecretKey key = Keys.hmacShaKeyFor(Objects.requireNonNull(environment.getProperty("token.secret")).getBytes(StandardCharsets.UTF_8));
            String authorities = authentication.getAuthorities().stream()
                    .map(Object::toString).collect(Collectors.joining(","));
            String jwt = Jwts.builder()
                    .setIssuer("Clinic Appointment API")
                    .setSubject("JWT Token")
                    .claim("username", authentication.getName())
                    .claim("authorities", authorities)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(Objects.requireNonNull(environment.getProperty("token.expiration_time")))))
                    .signWith(key)
                    .compact();
            response.setHeader(HttpHeaders.AUTHORIZATION, jwt);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().equals("/authenticate");

    }
}
