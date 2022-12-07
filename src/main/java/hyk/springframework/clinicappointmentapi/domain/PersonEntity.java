package hyk.springframework.clinicappointmentapi.domain;

import hyk.springframework.clinicappointmentapi.enums.Gender;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
public class PersonEntity extends NamedEntity {
    private LocalDate dateOfBirth;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String address;

    private String phoneNumber;
}
