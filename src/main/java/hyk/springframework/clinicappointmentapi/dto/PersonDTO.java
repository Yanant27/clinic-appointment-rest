package hyk.springframework.clinicappointmentapi.dto;

import hyk.springframework.clinicappointmentapi.enums.Gender;
import hyk.springframework.clinicappointmentapi.validation.ValidPhoneNumber;
import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PersonDTO extends NamedDTO{

    private Long age;

    private Gender gender;

    private String address;

    @NotEmpty
    @ValidPhoneNumber
    private String phoneNumber;

    public PersonDTO(Long id, String name, Long age, Gender gender, String address, String phoneNumber) {
        super(id, name);
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
