package hyk.springframework.clinicappointmentapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hyk.springframework.clinicappointmentapi.domain.security.User;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Doctor extends PersonEntity {

    @Builder
    public Doctor(String name, String address, String phoneNumber, String degree, String specialization) {
        super(name, address, phoneNumber);
        this.degree = degree;
        this.specialization = specialization;
    }

    private String degree;
    private String specialization;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();

    public Doctor addSchedule(Schedule schedule) {
        schedule.setDoctor(this);
        this.schedules.add(schedule);
        return this;
    }
}
