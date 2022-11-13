package hyk.springframework.clinicappointmentapi.dto.schedule;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class ScheduleResponseDTO extends ScheduleRequestDTO {

    private String doctorName;

    private String specialization;
}
