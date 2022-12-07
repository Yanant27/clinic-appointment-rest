package hyk.springframework.clinicappointmentapi.dto.schedule;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ScheduleRegistrationDTO {
    @NotNull
    private String dayOfWeek;

    @NotNull
    private String timeslot;

    @NotNull
    private Long doctorId;
}
