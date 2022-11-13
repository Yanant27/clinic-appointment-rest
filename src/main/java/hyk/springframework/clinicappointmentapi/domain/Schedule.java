package hyk.springframework.clinicappointmentapi.domain;

import hyk.springframework.clinicappointmentapi.enums.ScheduleStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
public class Schedule extends BaseEntity {

    private String dayOfWeek;

    private String timeslot;

    @Enumerated(value = EnumType.STRING)
    private ScheduleStatus scheduleStatus;

    @ManyToOne
    private Doctor doctor;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<Appointment> appointments = new ArrayList<>();
}
