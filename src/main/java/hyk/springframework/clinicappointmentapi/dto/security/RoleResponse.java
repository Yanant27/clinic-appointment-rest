package hyk.springframework.clinicappointmentapi.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {
    private Long id;

    private String name;
}
