package hyk.springframework.clinicappointmentapi.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * DTO for user log in and update username and password.
 *
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthenticationDTO {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
