package hyk.springframework.clinicappointmentapi.repository;

import hyk.springframework.clinicappointmentapi.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByDoctorId(Long doctorId);
}
