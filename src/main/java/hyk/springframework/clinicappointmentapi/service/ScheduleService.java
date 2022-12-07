package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleUpdateDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleRegistrationDTO;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface ScheduleService {
    List<ScheduleResponseDTO> findAllSchedules();
    ScheduleResponseDTO findScheduleById(Long scheduleId);

    ScheduleResponseDTO saveNewSchedule(ScheduleRegistrationDTO scheduleRegistrationDTO);

    ScheduleResponseDTO updateSchedule(Long scheduleId, ScheduleUpdateDTO scheduleUpdateDTO);

    void deleteScheduleById(Long scheduleId);

    List<ScheduleResponseDTO> findAllSchedulesByDoctorId(Long doctorId);
}
