package hyk.springframework.clinicappointmentapi.domain;

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
public class NamedEntity extends BaseEntity{
    private String name;
}
