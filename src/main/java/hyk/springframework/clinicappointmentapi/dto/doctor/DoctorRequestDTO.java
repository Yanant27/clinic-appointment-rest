package hyk.springframework.clinicappointmentapi.dto.doctor;

import hyk.springframework.clinicappointmentapi.dto.PersonDTO;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DoctorRequestDTO extends PersonDTO {

    @NotNull
    private String qualifications;

    @NotNull
    private String specialization;

    public DoctorRequestDTO(Long id, String name, Long age, Gender gender, String address, String phoneNumber, String qualifications, String specialization) {
        super(id, name, age, gender, address, phoneNumber);
        this.qualifications = qualifications;
        this.specialization = specialization;
    }
}
