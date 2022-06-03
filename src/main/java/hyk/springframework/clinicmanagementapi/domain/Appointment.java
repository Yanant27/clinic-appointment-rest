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
    @ManyToOne(cascade = CascadeType.MERGE)
    private Patient patient;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Doctor doctor;

    @OneToOne(cascade = CascadeType.MERGE)
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    private Status status;
}
