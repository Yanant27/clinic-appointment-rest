package hyk.springframework.clinicappointmentapi.dto.patient;

import hyk.springframework.clinicappointmentapi.dto.PersonDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class PatientRequestDTO extends PersonDTO {
}
