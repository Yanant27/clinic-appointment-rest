package hyk.springframework.clinicappointmentapi.web.mapper;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.web.dto.AppointmentDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Htoo Yanant Khin
 **/
@Mapper
@DecoratedWith(AppointmentMapperDecorator.class)
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

AppointmentDTO appointmentToAppointmentDto(Appointment appointment);

Appointment appointmentDtoToAppointment(AppointmentDTO appointmentDTO);
}
