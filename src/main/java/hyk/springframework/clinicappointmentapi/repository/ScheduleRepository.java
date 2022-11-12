package hyk.springframework.clinicappointmentapi.repository;

import hyk.springframework.clinicappointmentapi.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Htoo Yanant Khin
 **/
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByDoctorId(Long doctorId);

    Optional<Schedule> findByIdAndDoctorId(Long scheduleId, Long doctorId);
}
