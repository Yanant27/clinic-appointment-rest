package hyk.springframework.clinicappointmentapi.dto.patient;

import hyk.springframework.clinicappointmentapi.dto.PersonDTO;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PatientRequestDTO extends PersonDTO {
    public PatientRequestDTO(Long id, String name, Long age, Gender gender, String address, String phoneNumber) {
        super(id, name, age, gender, address, phoneNumber);
    }
}
