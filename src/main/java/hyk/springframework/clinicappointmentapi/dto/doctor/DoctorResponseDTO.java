package hyk.springframework.clinicappointmentapi.dto.doctor;

import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class DoctorResponseDTO extends DoctorRequestDTO {
    private List<ScheduleResponseDTO> scheduleResponseDTOS;

    private List<AppointmentResponseDTO> appointmentResponseDTOS;

    public DoctorResponseDTO(Long id, String name, Long age, Gender gender, String address, String phoneNumber, String qualifications, String specialization, List<ScheduleResponseDTO> scheduleResponseDTOS, List<AppointmentResponseDTO> appointmentResponseDTOS) {
        super(id, name, age, gender, address, phoneNumber, qualifications, specialization);
        this.scheduleResponseDTOS = scheduleResponseDTOS;
        this.appointmentResponseDTOS = appointmentResponseDTOS;
    }
}
