package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.web.dto.AppointmentDTO;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
public interface AppointmentService {
    List<AppointmentDTO> findAllAppointments(Long doctorId, Long patientId, Long scheduleId);

    AppointmentDTO findAppointmentById(Long appointmentId);

    AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO);

    void deleteAppointmentById(Long appointmentId);
}
