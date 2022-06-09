package hyk.springframework.clinicappointmentapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * @author Htoo Yanant Khin
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class PersonEntity extends BaseEntity {
    private String name;
    private String address;
    private String phoneNumber;
}
