package hyk.springframework.clinicappointmentapi.domain;

import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * @author Htoo Yanant Khin
 **/
@Getter
@Setter
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

    // logged in username or email
    private String creator;

    @Builder
    public Appointment(Long id, Integer version, Timestamp createdDate, Timestamp lastModifiedDate, Doctor doctor, Patient patient, Schedule schedule, LocalDate appointmentDate, AppointmentStatus appointmentStatus, String creator) {
        super(id, version, createdDate, lastModifiedDate);
        this.doctor = doctor;
        this.patient = patient;
        this.schedule = schedule;
        this.appointmentDate = appointmentDate;
        this.appointmentStatus = appointmentStatus;
        this.creator = creator;
    }
}