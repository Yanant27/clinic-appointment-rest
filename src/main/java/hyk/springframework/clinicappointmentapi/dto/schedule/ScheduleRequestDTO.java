package hyk.springframework.clinicappointmentapi.dto.schedule;

import hyk.springframework.clinicappointmentapi.dto.BaseDTO;
import hyk.springframework.clinicappointmentapi.enums.ScheduleStatus;
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
public class ScheduleRequestDTO extends BaseDTO {

    @NotNull
    private String dayOfWeek;

    @NotNull
    private String timeslot;

    private ScheduleStatus scheduleStatus;

    private Long doctorId;
}
