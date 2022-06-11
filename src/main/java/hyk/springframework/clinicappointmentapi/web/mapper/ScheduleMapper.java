package hyk.springframework.clinicappointmentapi.web.mapper;

import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.web.dto.ScheduleDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Htoo Yanant Khin
 **/
@Mapper
@DecoratedWith(ScheduleMapperDecorator.class)
public interface ScheduleMapper {
    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    ScheduleDTO scheduleToScheduleDto(Schedule schedule);

    Schedule scheduleDtoToSchedule(ScheduleDTO scheduleDTO);
}
