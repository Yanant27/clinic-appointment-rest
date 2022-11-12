package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface ScheduleService {
    List<ScheduleResponseDTO> findAllSchedules();
    ScheduleResponseDTO findScheduleById(Long scheduleId);

    ScheduleResponseDTO saveNewSchedule(ScheduleRequestDTO scheduleRequestDTO);

    ScheduleResponseDTO updateSchedule(Long scheduleId, ScheduleRequestDTO scheduleRequestDTO);

    void deleteScheduleById(Long scheduleId);

    List<ScheduleResponseDTO> findAllSchedulesByDoctorId(Long doctorId);

    void logicalDeleteScheduleByDoctorId(Long scheduleId, Long doctorId);
}
