package hyk.springframework.clinicappointmentapi.domain;

import hyk.springframework.clinicappointmentapi.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Patient extends PersonEntity {

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE)
    private List<Appointment> appointments;

    @Builder
    public Patient(String name, Long age, Gender gender, String address, String phoneNumber, List<Appointment> appointments) {
        super(name, age, gender, address, phoneNumber);
        this.appointments = appointments;
    }
}
