package hyk.springframework.clinicappointmentapi.domain;

import hyk.springframework.clinicappointmentapi.enums.Gender;
import lombok.*;

import javax.persistence.MappedSuperclass;

/**
 * @author Htoo Yanant Khin
 **/
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class PersonEntity extends NamedEntity {
    private Long age;
    private Gender gender;
    private String address;
    private String phoneNumber;

    public PersonEntity(String name, Long age, Gender gender, String address, String phoneNumber) {
        super(name);
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
