package hyk.springframework.clinicappointmentapi.repository;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Htoo Yanant Khin
 **/
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
