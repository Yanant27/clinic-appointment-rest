package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.dto.security.UserAuthenticationDTO;
import hyk.springframework.clinicappointmentapi.dto.security.UserRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.security.UserResponse;
import hyk.springframework.clinicappointmentapi.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Htoo Yanant Khin
 **/
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registerAdmin")
    public ResponseEntity<UserResponse> registerAdmin(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        return new ResponseEntity<>(userService.saveNewUser(userRegistrationDTO), HttpStatus.CREATED);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<UserResponse> authenticateUser(@Valid @RequestBody UserAuthenticationDTO userAuthenticationDTO) {
        return new ResponseEntity<>(userService.authenticateUser(userAuthenticationDTO), HttpStatus.OK);
    }
}
