package hyk.springframework.clinicappointmentapi.security.authmanager;

import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DoctorAuthorizationManger {
    private final UserRepository userRepository;

    public boolean doctorIdMatches(Authentication authentication, Long doctorId){
        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();

        return authenticatedUser.getDoctor().getId().equals(doctorId);
    }
}