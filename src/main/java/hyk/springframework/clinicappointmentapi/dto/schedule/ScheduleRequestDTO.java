package hyk.springframework.clinicappointmentapi.dto.schedule;

import hyk.springframework.clinicappointmentapi.dto.BaseDTO;
import hyk.springframework.clinicappointmentapi.enums.ScheduleStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ScheduleRequestDTO extends BaseDTO {

    @NotNull
    private String dayOfWeek;

    @NotNull
    private String timeslot;

    private ScheduleStatus scheduleStatus;

    private Long doctorId;

    public ScheduleRequestDTO(Long id, String dayOfWeek, String timeslot, ScheduleStatus scheduleStatus, Long doctorId) {
        super(id);
        this.dayOfWeek = dayOfWeek;
        this.timeslot = timeslot;
        this.scheduleStatus = Objects.requireNonNullElse(scheduleStatus, ScheduleStatus.AVAILABLE);
        this.doctorId = doctorId;
    }
}
