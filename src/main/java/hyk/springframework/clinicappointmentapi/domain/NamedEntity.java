package hyk.springframework.clinicappointmentapi.domain;

import lombok.*;

import javax.persistence.MappedSuperclass;

/**
 * @author Htoo Yanant Khin
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class NamedEntity extends BaseEntity{
    private String name;
}
