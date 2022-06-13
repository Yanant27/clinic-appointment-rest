package hyk.springframework.clinicappointmentapi.web.dto.security;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author Htoo Yanant Khin
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 2709469567051836719L;
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
