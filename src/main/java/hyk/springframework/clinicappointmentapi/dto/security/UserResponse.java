package hyk.springframework.clinicappointmentapi.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO for retrieving user login info
 *
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;

    private String username;

    private Set<RoleResponse> roles;

    private Boolean accountNonLocked;

    private Boolean enabled;
}
