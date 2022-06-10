package hyk.springframework.clinicappointmentapi.repository;

import hyk.springframework.clinicappointmentapi.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Htoo Yanant Khin
 **/
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
