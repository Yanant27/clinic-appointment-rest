package hyk.springframework.clinicappointmentapi.domain;

import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
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

    // logged in username or email
    private String creator;
}