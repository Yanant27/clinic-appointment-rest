package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleRegistrationDTO;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Htoo Yanant Khin
 **/
public abstract class ScheduleMapperDecorator implements ScheduleMapper {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    @Qualifier("delegate")
    private ScheduleMapper scheduleMapper;

    @Override
    public Schedule scheduleRegistrationDtoToSchedule(ScheduleRegistrationDTO scheduleRegistrationDTO) {
        Schedule schedule = scheduleMapper.scheduleRegistrationDtoToSchedule(scheduleRegistrationDTO);
       // Update data
        schedule.setDayOfWeek(scheduleRegistrationDTO.getDayOfWeek());
        schedule.setTimeslot(scheduleRegistrationDTO.getTimeslot());
        schedule.setDoctor(doctorRepository.findById(scheduleRegistrationDTO.getDoctorId())
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Doctor Not Found. ID: " + scheduleRegistrationDTO.getDoctorId());
                }));
        return schedule;
    }
}
