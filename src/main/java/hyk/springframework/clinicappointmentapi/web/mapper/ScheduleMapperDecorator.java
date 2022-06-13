package hyk.springframework.clinicappointmentapi.web.mapper;

import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import hyk.springframework.clinicappointmentapi.web.dto.ScheduleDTO;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Htoo Yanant Khin
 **/
public abstract class ScheduleMapperDecorator implements ScheduleMapper{
    private DoctorRepository doctorRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleMapper scheduleMapper;
    
    @Autowired
    public void setDoctorRepository(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Autowired
    public void setScheduleRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }
    
    @Autowired
    @Qualifier("delegate")
    public void setScheduleMapper(ScheduleMapper scheduleMapper) {
        this.scheduleMapper = scheduleMapper;
    }

    @Override
    public ScheduleDTO scheduleToScheduleDto(Schedule schedule) {
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDto(schedule);
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setStartTime(schedule.getStartTime());
        scheduleDTO.setEndTime(schedule.getEndTime());
        scheduleDTO.setDoctorId(schedule.getDoctor().getId());
        scheduleDTO.setDoctorName(schedule.getDoctor().getName());
        scheduleDTO.setDoctorStatus(schedule.getDoctorStatus());
        return scheduleDTO;
    }

    @Override
    public Schedule scheduleDtoToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule;
        if (scheduleDTO.getId() != null) {
            schedule = scheduleRepository.findById(scheduleDTO.getId()).orElse(new Schedule());
        } else {
            schedule = scheduleMapper.scheduleDtoToSchedule(scheduleDTO);
            schedule.setDoctor(doctorRepository.findById(scheduleDTO.getDoctorId()).orElseThrow(NotFoundException::new));
        }
        schedule.setDate(scheduleDTO.getDate());
        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setEndTime(scheduleDTO.getEndTime());
        return schedule;
    }
}
