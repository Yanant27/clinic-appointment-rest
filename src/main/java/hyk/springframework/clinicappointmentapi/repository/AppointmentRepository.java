package hyk.springframework.clinicappointmentapi.repository;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Htoo Yanant Khin
 **/
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    List<Appointment> findAllByDoctorId(Long doctorId);

    List<Appointment> findAllByPatientId(Long patientid);

    List<Appointment> findAllByScheduleId(Long scheduleId);
}
