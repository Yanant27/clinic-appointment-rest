package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Htoo Yanant Khin
 **/
@Mapper
@DecoratedWith(AppointmentMapperDecorator.class)
public interface AppointmentMapper {
    // Create new appointment
    Appointment appointmentRegistrationDtoToAppointment(AppointmentRegistrationDTO appointmentRegistrationDTO);

    // Retrieve appointment by Id
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", source = "doctor.name")
    @Mapping(target = "specialization", source = "doctor.specialization")
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", source = "patient.name")
    @Mapping(target = "patientPhoneNumber", source = "patient.phoneNumber")
    AppointmentResponseDTO appointmentToAppointmentResponseDto(Appointment appointment);
}
