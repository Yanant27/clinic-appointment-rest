package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Htoo Yanant Khin
 **/
@Mapper
@DecoratedWith(ScheduleMapperDecorator.class)
public interface ScheduleMapper {
    // Retrieve schedule by Id
    @Mapping(target = "doctorId", source = "doctor.id")
    ScheduleResponseDTO scheduleToScheduleResponseDto(Schedule schedule);

    Schedule scheduleRegistrationDtoToSchedule(ScheduleRegistrationDTO scheduleRegistrationDTO);
}
