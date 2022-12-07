package hyk.springframework.clinicappointmentapi.dto;

import hyk.springframework.clinicappointmentapi.enums.Gender;
import hyk.springframework.clinicappointmentapi.validation.ValidPhoneNumber;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PersonDTO {
    @NotEmpty
    private String name;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String address;

    @NotEmpty
    @ValidPhoneNumber
    private String phoneNumber;
}
