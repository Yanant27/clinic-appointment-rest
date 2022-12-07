package hyk.springframework.clinicappointmentapi.repository;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByDoctorId(Long doctorId);

    List<Appointment> findAllByPatientId(Long patientId);
}
