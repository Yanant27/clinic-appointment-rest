package hyk.springframework.clinicappointmentapi.service.security;

import hyk.springframework.clinicappointmentapi.dto.security.UserAuthenticationDTO;
import hyk.springframework.clinicappointmentapi.dto.security.UserRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.security.UserResponse;

/**
 * @author Htoo Yanant Khin
 **/
public interface UserService {
    UserResponse authenticateUser(UserAuthenticationDTO userAuthenticationDTO);

    UserResponse saveNewUser(UserRegistrationDTO userRegistrationDTO);
}
