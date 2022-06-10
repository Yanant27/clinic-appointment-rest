package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.web.dto.ScheduleDTO;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface ScheduleService {
    List<ScheduleDTO> findAllSchedules(Long scheduleId);

    ScheduleDTO findScheduleById(Long scheduleId);

    ScheduleDTO saveNewSchedule(ScheduleDTO scheduleDTO);

    ScheduleDTO updateSchedule(Long scheduleId, ScheduleDTO scheduleDTO);

    void deleteScheduleById(Long scheduleId);
}
