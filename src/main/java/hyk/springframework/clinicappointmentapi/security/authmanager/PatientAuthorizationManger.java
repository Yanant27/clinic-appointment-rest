package hyk.springframework.clinicappointmentapi.security.authmanager;

import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PatientAuthorizationManger {
    private final UserRepository userRepository;

    public boolean patientIdMatches(Authentication authentication, Long patientId){
        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();

        return authenticatedUser.getPatient().getId().equals(patientId);
    }

}