package hyk.springframework.clinicappointmentapi.repository;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllBySpecialization(String specialization);
}
