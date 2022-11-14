package hyk.springframework.clinicappointmentapi.dto;

import hyk.springframework.clinicappointmentapi.enums.Gender;
import hyk.springframework.clinicappointmentapi.validation.ValidPhoneNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class PersonDTO extends NamedDTO{

    private Long age;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String address;

    @NotEmpty
    @ValidPhoneNumber
    private String phoneNumber;
}
