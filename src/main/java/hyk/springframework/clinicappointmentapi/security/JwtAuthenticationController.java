package hyk.springframework.clinicappointmentapi.security;

import hyk.springframework.clinicappointmentapi.dto.security.JwtRequest;
import hyk.springframework.clinicappointmentapi.dto.security.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Htoo Yanant Khin
 **/
@RestController
@RequiredArgsConstructor
public class JwtAuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider tokenProvider;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody JwtRequest jwtRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
