package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Htoo Yanant Khin
 **/
@Mapper
@DecoratedWith(ScheduleMapperDecorator.class)
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    // Create or update schedule
    Schedule scheduleRequestDtoToSchedule(ScheduleRequestDTO scheduleRequestDTO);

    // Retrieve schedule by Id
    @Mapping(target = "specialization", source = "doctor.specialization")
    @Mapping(target = "doctorName", source = "doctor.name")
    @Mapping(target = "doctorId", source = "doctor.id")
    ScheduleResponseDTO scheduleToScheduleResponseDto(Schedule schedule);
}
