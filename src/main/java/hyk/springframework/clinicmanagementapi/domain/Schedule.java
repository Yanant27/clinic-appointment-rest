package hyk.springframework.clinicmanagementapi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Htoo Yanant Khin
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Schedule extends BaseEntity {
    private LocalDate scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne
    private Doctor doctor;

    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL)
    private Appointment appointment;
}
