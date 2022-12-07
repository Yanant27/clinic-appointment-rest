package hyk.springframework.clinicappointmentapi.security.authmanager;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppointmentAuthorizationManger {
    private final UserRepository userRepository;

    private final AppointmentRepository appointmentRepository;

    public boolean appointmentIdMatches(Authentication authentication, Long appointmentId){
        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> {
                    throw new ResourceNotFoundException("Appointment Not Found. ID: " + appointmentId);
                });

        return authenticatedUser.getDoctor().getId().equals(appointment.getDoctor().getId());
    }
}