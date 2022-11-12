package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentUpdateStatusDTO;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface AppointmentService {
    List<AppointmentResponseDTO> findAllAppointments();

    AppointmentResponseDTO findAppointmentById(Long appointmentId);

    AppointmentResponseDTO saveAppointment(AppointmentRequestDTO appointmentRequestDTO);

    AppointmentResponseDTO updateAppointmentStatus(Long appointmentId, AppointmentUpdateStatusDTO appointmentUpdateStatusDTO);

    void deleteAppointmentById(Long appointmentId);

    List<AppointmentResponseDTO> findAllAppointmentsByDoctorId(Long doctorId);

    List<AppointmentResponseDTO> findAllAppointmentsByPatientId(Long patientId);
}
