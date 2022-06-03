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
public class Patient extends PersonEntity {
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;
}
