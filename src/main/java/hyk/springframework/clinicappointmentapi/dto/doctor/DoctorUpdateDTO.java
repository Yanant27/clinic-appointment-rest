package hyk.springframework.clinicappointmentapi.dto.doctor;

import hyk.springframework.clinicappointmentapi.dto.PersonDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class DoctorUpdateDTO extends PersonDTO {
    @NotNull
    private Long id;

    @NotEmpty
    private String qualifications;

    @NotEmpty
    private String specialization;
}
