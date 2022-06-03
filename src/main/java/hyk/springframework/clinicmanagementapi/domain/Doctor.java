package hyk.springframework.clinicmanagementapi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Doctor extends PersonEntity {
    private String specialization;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;
}
