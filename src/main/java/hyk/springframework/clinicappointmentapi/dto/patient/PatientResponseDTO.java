package hyk.springframework.clinicappointmentapi.dto.patient;

import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class PatientResponseDTO extends PatientRequestDTO {
    private List<AppointmentResponseDTO> appointments;

    @Builder
    public PatientResponseDTO(Long id, String name, Long age, Gender gender, String address, String phoneNumber, List<AppointmentResponseDTO> appointments) {
        super(id, name, age, gender, address, phoneNumber);
        this.appointments = appointments;
    }
}
