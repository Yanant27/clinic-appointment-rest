package hyk.springframework.clinicappointmentapi.security.authmanager;

import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import hyk.springframework.clinicappointmentapi.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ScheduleAuthorizationManger {
    private final UserRepository userRepository;

    private final ScheduleRepository scheduleRepository;

    public boolean scheduleIdMatches(Authentication authentication, Long scheduleId){
        User authenticatedUser = userRepository.findByUsername(authentication.getName()).get();

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> {
                    throw new ResourceNotFoundException("Schedule Not Found. ID: " + scheduleId);
                });

        return authenticatedUser.getDoctor().getId().equals(schedule.getDoctor().getId());
    }
}