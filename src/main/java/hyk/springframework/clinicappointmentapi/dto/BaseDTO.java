package hyk.springframework.clinicappointmentapi.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Htoo Yanant Khin
 **/
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class BaseDTO {
    private Long id;
}
