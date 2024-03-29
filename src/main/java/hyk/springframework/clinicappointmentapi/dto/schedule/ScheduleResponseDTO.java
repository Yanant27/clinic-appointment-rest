package hyk.springframework.clinicappointmentapi.dto.schedule;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ScheduleResponseDTO extends ScheduleUpdateDTO {
    private String createdBy;

    private String modifiedBy;
}
