package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentUpdateStatusDTO;
import hyk.springframework.clinicappointmentapi.dto.mapper.AppointmentMapper;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;

    private final AppointmentMapper appointmentMapper;

    @Override
    public List<AppointmentResponseDTO> findAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::appointmentToAppointmentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDTO findAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map(appointmentMapper::appointmentToAppointmentResponseDto)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Appointment Not Found. ID: " + appointmentId);
                });
    }

    @Override
    @Transactional
    public AppointmentResponseDTO saveNewAppointment(AppointmentRegistrationDTO appointmentRegistrationDTO) {
        Appointment appointment = appointmentMapper.appointmentRegistrationDtoToAppointment(appointmentRegistrationDTO);
        appointment.setCreatedBy(LoginUserUtil.getLoginUsername());
        appointment.setModifiedBy(LoginUserUtil.getLoginUsername());
        // Set default status
        appointment.setAppointmentStatus(AppointmentStatus.PENDING);
        return appointmentMapper.appointmentToAppointmentResponseDto(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public AppointmentResponseDTO updateAppointmentStatus(Long appointmentId, AppointmentUpdateStatusDTO appointmentUpdateStatusDTO) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Appointment Not Found. ID: " + appointmentId);
                });
        appointment.setAppointmentStatus(appointmentUpdateStatusDTO.getAppointmentStatus());
        appointment.setModifiedBy(LoginUserUtil.getLoginUsername());
        return appointmentMapper.appointmentToAppointmentResponseDto(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public void deleteAppointmentById(Long appointmentId) {
        appointmentRepository.findById(appointmentId)
                .ifPresentOrElse(
                        appointment -> appointmentRepository.deleteById(appointmentId),
                        () -> {
                            throw new ResourceNotFoundException("Appointment Not Found. ID: " + appointmentId);
                        });
    }

    @Override
    public List<AppointmentResponseDTO> findAllAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findAllByDoctorId(doctorId).stream()
                .map(appointmentMapper::appointmentToAppointmentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDTO> findAllAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findAllByPatientId(patientId).stream()
                .map(appointmentMapper::appointmentToAppointmentResponseDto)
                .collect(Collectors.toList());
    }

    /*
    Check requested appointment date is valid or not. <br>
     e.g Doctor available day of week - Sunday, Monday <br>
         Requested appointment date - 2022-12-30 Friday (invalid appointment date) <br>
     */
//    private boolean checkAppointmentDate(AppointmentRegistrationDTO appointmentRegistrationDTO) {
//        String dayOfWeek = appointmentRegistrationDTO.getAppointmentDate().getDayOfWeek().toString();
//        return scheduleService.findAllSchedulesByDoctorId(appointmentRegistrationDTO.getDoctorId()).stream()
//                .anyMatch(scheduleDTO -> scheduleDTO.getDayOfWeek().equals(dayOfWeek) &&
//                                scheduleDTO.getTimeslot().equals(appointmentRegistrationDTO.getTimeslot()));
//    }
}
