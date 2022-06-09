package hyk.springframework.clinicappointmentapi.domain;

import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * @author Htoo Yanant Khin
 **/
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Appointment extends BaseEntity {
    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Schedule schedule;

    private LocalDate appointmentDate;

    @Enumerated(value = EnumType.STRING)
    private AppointmentStatus appointmentStatus;
}