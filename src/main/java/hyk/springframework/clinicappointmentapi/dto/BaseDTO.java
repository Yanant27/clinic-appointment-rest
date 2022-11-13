package hyk.springframework.clinicappointmentapi.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Htoo Yanant Khin
 **/
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class BaseDTO {
    private Long id;
}
