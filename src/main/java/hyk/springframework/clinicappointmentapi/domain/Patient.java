package hyk.springframework.clinicappointmentapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Patient extends PersonEntity {
    @Builder
    public Patient(String name, String address, String phoneNumber) {
        super(name, address, phoneNumber);
    }

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointments;

    public Patient addAppointMent(Appointment appointment) {
        appointment.setPatient(this);
        this.appointments.add(appointment);
        return this;
    }
}
