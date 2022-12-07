package hyk.springframework.clinicappointmentapi.dto.security;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * DTO for user log in and update username and password.
 *
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationDTO {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private Set<String> roles;
}
