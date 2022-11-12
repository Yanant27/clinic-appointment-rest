package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentUpdateStatusDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Htoo Yanant Khin
 **/
@Mapper
@DecoratedWith(AppointmentMapperDecorator.class)
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    // Create new appointment
    Appointment appointmentRequestDtoToAppointment(AppointmentRequestDTO appointmentRequestDTO);

    // Retrieve appointment by Id
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", source = "doctor.name")
    @Mapping(target = "specialization", source = "doctor.specialization")
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", source = "patient.name")
    @Mapping(target = "patientPhoneNumber", source = "patient.phoneNumber")
    @Mapping(target = "timeslot", source = "schedule.timeslot")
    @Mapping(target = "scheduleId", source = "schedule.id")
    AppointmentResponseDTO appointmentToAppointmentResponseDto(Appointment appointment);

    // Only for update appointment status
    Appointment AppointmentUpdateStatusDtoToAppointment(AppointmentUpdateStatusDTO appointmentUpdateStatusDTO);
}
