package hyk.springframework.clinicmanagementapi.domain;

import hyk.springframework.clinicmanagementapi.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author Htoo Yanant Khin
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Appointment extends BaseEntity {
    @ManyToOne
    private Patient patient;

    @OneToOne
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    private Status status;
}
