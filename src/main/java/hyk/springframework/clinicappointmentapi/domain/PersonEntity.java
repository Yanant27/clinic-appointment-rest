package hyk.springframework.clinicappointmentapi.domain;

import hyk.springframework.clinicappointmentapi.enums.Gender;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
public class PersonEntity extends NamedEntity {
    private Long age;
    private Gender gender;
    private String address;
    private String phoneNumber;
}
