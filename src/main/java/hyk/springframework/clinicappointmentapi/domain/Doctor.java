package hyk.springframework.clinicappointmentapi.domain;

import hyk.springframework.clinicappointmentapi.domain.security.User;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
public class Doctor extends PersonEntity {

    private String qualifications;

    private String specialization;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Appointment> appointments = new ArrayList<>();
}
