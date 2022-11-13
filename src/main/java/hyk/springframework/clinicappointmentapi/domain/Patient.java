package hyk.springframework.clinicappointmentapi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
public class Patient extends PersonEntity {

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private List<Appointment> appointments;
}
