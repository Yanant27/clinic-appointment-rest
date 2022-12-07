package hyk.springframework.clinicappointmentapi.dto.schedule;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ScheduleUpdateDTO extends ScheduleRegistrationDTO {
    @NotNull
    private Long id;
}
