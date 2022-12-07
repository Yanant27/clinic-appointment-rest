package hyk.springframework.clinicappointmentapi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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

    @ManyToOne
    private Doctor doctor;
}
