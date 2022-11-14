package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentUpdateStatusDTO;
import hyk.springframework.clinicappointmentapi.dto.mapper.AppointmentMapper;
import hyk.springframework.clinicappointmentapi.dto.mapper.PatientMapper;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
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

    private final PatientRepository patientRepository;

    private final AppointmentMapper appointmentMapper;

    private final PatientMapper patientMapper;

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
                    throw new NotFoundException("Appointment Not Found. ID: " + appointmentId);
                });
    }

    @Override
    @Transactional
    public AppointmentResponseDTO saveNewAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        // Set default appointment status for new appointment
        appointmentRequestDTO.setAppointmentStatus(AppointmentStatus.PENDING);
        // For new patient, register patient first.
        if (appointmentRequestDTO.getPatientRequestDTO().getId() == null) {
            Patient patient = patientRepository.save(patientMapper.patientRequestDtoToPatient(appointmentRequestDTO.getPatientRequestDTO()));
            appointmentRequestDTO.setPatientRequestDTO(patientMapper.patientToPatientRequestDto(patient));
        }
        return appointmentMapper.appointmentToAppointmentResponseDto(
                appointmentRepository.save(appointmentMapper.appointmentRequestDtoToAppointment(appointmentRequestDTO)));
    }

    @Override
    @Transactional
    public AppointmentResponseDTO updateAppointmentStatus(Long appointmentId, AppointmentUpdateStatusDTO appointmentUpdateStatusDTO) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Appointment Not Found. ID: " + appointmentId);
                });
        appointment.setAppointmentStatus(appointmentUpdateStatusDTO.getAppointmentStatus());
        return appointmentMapper.appointmentToAppointmentResponseDto(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public void deleteAppointmentById(Long appointmentId) {
        appointmentRepository.findById(appointmentId)
                .ifPresentOrElse(
                        appointment -> appointmentRepository.deleteById(appointmentId),
                        () -> {
                            throw new NotFoundException("Appointment Not Found. ID: " + appointmentId);
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
}
