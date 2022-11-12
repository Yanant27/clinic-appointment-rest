package hyk.springframework.clinicappointmentapi.dto.schedule;

import hyk.springframework.clinicappointmentapi.enums.ScheduleStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleResponseDTO extends ScheduleRequestDTO {

    private String doctorName;

    private String specialization;

    @Builder
    public ScheduleResponseDTO(Long id, String dayOfWeek, String timeslot, ScheduleStatus scheduleStatus, Long doctorId, String doctorName, String specialization) {
        super(id, dayOfWeek, timeslot, scheduleStatus, doctorId);
        this.doctorName = doctorName;
        this.specialization = specialization;
    }
}
