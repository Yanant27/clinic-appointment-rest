package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Htoo Yanant Khin
 **/
public abstract class ScheduleMapperDecorator implements ScheduleMapper {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    @Qualifier("delegate")
    private ScheduleMapper scheduleMapper;

    @Override
    public Schedule scheduleRequestDtoToSchedule(ScheduleRequestDTO scheduleRequestDTO) {
        if (scheduleRequestDTO == null) {
            return null;
        }
        Schedule schedule;
        if (scheduleRequestDTO.getId() != null) {
            // Update existing schedule case
            schedule = scheduleRepository.findById(scheduleRequestDTO.getId())
                    .orElseThrow(() -> {
                        throw new NotFoundException("Schedule Not Found. ID: " + scheduleRequestDTO.getId());
                    });
        } else {
            // Register new schedule case
            schedule = scheduleMapper.scheduleRequestDtoToSchedule(scheduleRequestDTO);
        }
        // Update data
        schedule.setDayOfWeek(scheduleRequestDTO.getDayOfWeek());
        schedule.setTimeslot(scheduleRequestDTO.getTimeslot());
        schedule.setScheduleStatus(scheduleRequestDTO.getScheduleStatus());
        schedule.setDoctor(doctorRepository.findById(scheduleRequestDTO.getDoctorId())
                .orElseThrow(() -> {
                    throw new NotFoundException("Doctor Not Found. ID: " + scheduleRequestDTO.getDoctorId());
                }));
        return schedule;
    }

    @Override
    public ScheduleResponseDTO scheduleToScheduleResponseDto(Schedule schedule) {
        if (schedule == null) {
            return null;
        }
        ScheduleResponseDTO scheduleResponseDTO = scheduleMapper.scheduleToScheduleResponseDto(schedule);
        scheduleResponseDTO.setId(schedule.getId());
        scheduleResponseDTO.setDayOfWeek(schedule.getDayOfWeek());
        scheduleResponseDTO.setTimeslot(schedule.getTimeslot());
        scheduleResponseDTO.setScheduleStatus(schedule.getScheduleStatus());
        // Available schedules might not assign to any doctor
        if (schedule.getDoctor() != null) {
            scheduleResponseDTO.setDoctorId(schedule.getDoctor().getId());
            scheduleResponseDTO.setDoctorName(schedule.getDoctor().getName());
            scheduleResponseDTO.setSpecialization(schedule.getDoctor().getSpecialization());
        }
        return scheduleResponseDTO;
    }
}
