package hyk.springframework.clinicappointmentapi.dto.doctor;

import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class DoctorResponseDTO extends DoctorUpdateDTO {
    @Builder.Default
    private List<ScheduleResponseDTO> scheduleResponseDTOS = new ArrayList<>();

    @Builder.Default
    private List<AppointmentResponseDTO> appointmentResponseDTOS  = new ArrayList<>();

    private String createdBy;

    private String modifiedBy;
}
