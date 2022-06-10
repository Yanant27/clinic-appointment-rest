package hyk.springframework.clinicappointmentapi.repository;

import hyk.springframework.clinicappointmentapi.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Htoo Yanant Khin
 **/
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
