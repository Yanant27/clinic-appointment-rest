package hyk.springframework.clinicappointmentapi.domain;

import hyk.springframework.clinicappointmentapi.enums.Gender;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Doctor extends PersonEntity {

    @Builder
    public Doctor(String name, Long age, Gender gender, String address, String phoneNumber, String qualifications, String specialization, List<Schedule> schedules, List<Appointment> appointments) {
        super(name, age, gender, address, phoneNumber);
        this.qualifications = qualifications;
        this.specialization = specialization;
        this.schedules = schedules;
        this.appointments = appointments;
    }

    private String qualifications;
    private String specialization;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Appointment> appointments = new ArrayList<>();
}
