package hyk.springframework.clinicappointmentapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hyk.springframework.clinicappointmentapi.enums.DoctorStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Schedule extends BaseEntity {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(value = EnumType.STRING)
    private DoctorStatus status;

    @ManyToOne
    private Doctor doctor;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointments;

    public Schedule addAppointment(Appointment appointment) {
        appointment.setSchedule(this);
        this.appointments.add(appointment);
        return this;
    }
}
