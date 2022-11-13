package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentUpdateStatusDTO;
import hyk.springframework.clinicappointmentapi.dto.mapper.AppointmentMapper;
import hyk.springframework.clinicappointmentapi.dto.mapper.PatientMapper;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientRequestDTO;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Htoo Yanant Khin
 **/
@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {
    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    PatientRepository patientRepository;

    @Mock
    AppointmentMapper appointmentMapper;

    @Mock
    PatientMapper patientMapper;

    @InjectMocks
    AppointmentServiceImpl appointmentService;

    @Test
    @DisplayName("Find all appointments - Success")
    void findAllAppointments_Success() {
        // Test data
        List<Appointment> appointments = Arrays.asList(
                Appointment.builder().build(),
                Appointment.builder().build());

        // Mock method call
        when(appointmentRepository.findAll()).thenReturn(appointments);

        List<AppointmentResponseDTO> result = appointmentService.findAllAppointments();

        // Verify
        assertEquals(2, result.size());
        verify(appointmentRepository, times(1)).findAll();
        verify(appointmentMapper, times(result.size())).appointmentToAppointmentResponseDto(any());
    }

    @Test
    @DisplayName("Find appointment by id - Success")
    void findAppointmentById_Success() {
        // Test data
        Appointment appointment = Appointment.builder().appointmentDate(LocalDate.now().plusDays(3)).build();
        AppointmentResponseDTO appointmentResponseDTO = AppointmentResponseDTO.builder().appointmentDate(LocalDate.now().plusDays(3)).build();

        // Mock method call
        when(appointmentMapper.appointmentToAppointmentResponseDto(any())).thenReturn(appointmentResponseDTO);
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        AppointmentResponseDTO returnDto = appointmentService.findAppointmentById(anyLong());

        // Verify
        assertNotNull(returnDto);
        assertEquals(appointment.getAppointmentDate(), returnDto.getAppointmentDate());
        verify(appointmentMapper, times(1)).appointmentToAppointmentResponseDto(any());
        verify(appointmentRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find appointment by id - Not found")
    void findAppointmentById_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                appointmentService.findAppointmentById(100L));

        // Verify
        assertEquals("Appointment Not Found. ID: 100", exception.getMessage());
        verify(appointmentRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Saving new appointment - New Patient")
    void saveAppointment_New_Patient() {
        // Test data
        Appointment appointment = Appointment.builder().appointmentDate(LocalDate.now().plusDays(3)).build();
        AppointmentRequestDTO appointmentRequestDTO = AppointmentRequestDTO.builder().appointmentDate(LocalDate.now().plusDays(3))
                .patientRequestDTO(PatientRequestDTO.builder().build()).build();
        AppointmentResponseDTO appointmentResponseDTO = AppointmentResponseDTO.builder().appointmentDate(LocalDate.now().plusDays(3)).patientId(1L).build();
        Patient patient = Patient.builder().id(1L).build();
        PatientRequestDTO patientRequestDTO = PatientRequestDTO.builder().id(1L).build();

        // Mock method call
        when(patientRepository.save(any())).thenReturn(patient);
        when(patientMapper.patientRequestDtoToPatient(any())).thenReturn(patient);
        when(patientMapper.patientToPatientRequestDto(any())).thenReturn(patientRequestDTO);
        when(appointmentMapper.appointmentRequestDtoToAppointment(any())).thenReturn(appointment);
        when(appointmentMapper.appointmentToAppointmentResponseDto(any())).thenReturn(appointmentResponseDTO);
        when(appointmentRepository.save(any())).thenReturn(appointment);

        AppointmentResponseDTO savedDto = appointmentService.saveNewAppointment(appointmentRequestDTO);

        // Verify
        assertEquals(appointmentResponseDTO, savedDto);
        assertEquals(1L, appointmentResponseDTO.getPatientId());
        assertEquals(appointment.getAppointmentDate(), savedDto.getAppointmentDate());
        verify(patientRepository, times(1)).save(any());
        verify(patientMapper, times(1)).patientRequestDtoToPatient(any());
        verify(patientMapper, times(1)).patientToPatientRequestDto(any());
        verify(appointmentMapper, times(1)).appointmentRequestDtoToAppointment(any());
        verify(appointmentMapper, times(1)).appointmentToAppointmentResponseDto(any());
        verify(appointmentRepository, times(1)).save(any());
    }

    @Disabled
    @Test
    @DisplayName("Saving new appointment - Old Patient")
    void saveAppointment_Old_Patient() {
        // Test data
        Appointment appointment = Appointment.builder().appointmentDate(LocalDate.now().plusDays(3)).build();
        AppointmentRequestDTO appointmentRequestDTO = AppointmentRequestDTO.builder().appointmentDate(LocalDate.now().plusDays(3))
                .patientRequestDTO(PatientRequestDTO.builder().id(1L).build()).build();
        AppointmentResponseDTO appointmentResponseDTO = AppointmentResponseDTO.builder().appointmentDate(LocalDate.now().plusDays(3)).patientId(1L).build();

        // Mock method call
        when(appointmentMapper.appointmentRequestDtoToAppointment(any())).thenReturn(appointment);
        when(appointmentMapper.appointmentToAppointmentResponseDto(any())).thenReturn(appointmentResponseDTO);
        when(appointmentRepository.save(any())).thenReturn(appointment);

        AppointmentResponseDTO savedDto = appointmentService.saveNewAppointment(appointmentRequestDTO);

        // Verify
        assertEquals(appointmentResponseDTO, savedDto);
        assertEquals(1L, appointmentResponseDTO.getPatientId());
        assertEquals(appointment.getAppointmentDate(), savedDto.getAppointmentDate());
        verify(appointmentMapper, times(1)).appointmentRequestDtoToAppointment(any());
        verify(appointmentMapper, times(1)).appointmentToAppointmentResponseDto(any());
        verify(appointmentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Update appointment information - Success")
    void updateAppointmentStatus_Success() {
        // Test data
        Appointment appointment = Appointment.builder().id(1L).appointmentDate(LocalDate.now().plusDays(3)).build();
        AppointmentUpdateStatusDTO appointmentUpdateStatusDTO = AppointmentUpdateStatusDTO.builder().id(1L).appointmentStatus(AppointmentStatus.APPROVED).build();
        AppointmentResponseDTO appointmentResponseDTO = AppointmentResponseDTO.builder().id(1L).appointmentStatus(AppointmentStatus.APPROVED).appointmentDate(LocalDate.now().plusDays(3)).build();

        // Mock method call
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        when(appointmentMapper.appointmentToAppointmentResponseDto(any())).thenReturn(appointmentResponseDTO);
        when(appointmentRepository.save(any())).thenReturn(appointment);

        AppointmentResponseDTO savedDto = appointmentService.updateAppointmentStatus(1L, appointmentUpdateStatusDTO);

        // Verify
        assertEquals(appointmentResponseDTO, savedDto);
        assertEquals(appointmentUpdateStatusDTO.getAppointmentStatus(), savedDto.getAppointmentStatus());
        verify(appointmentMapper, times(1)).appointmentToAppointmentResponseDto(any());
        verify(appointmentRepository, times(1)).save(any());
        verify(appointmentRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Update appointment information - Not found")
    void updateAppointmentStatus_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                appointmentService.updateAppointmentStatus(100L, any()));

        // Verify
        assertEquals("Appointment Not Found. ID: 100", exception.getMessage());
        verify(appointmentRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Delete appointment by id - Success")
    void deleteAppointmentById_Success() {
        // Test data
        Appointment appointment = Appointment.builder().id(1L).appointmentDate(LocalDate.now().plusDays(3)).build();

        // Mock method call
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        appointmentService.deleteAppointmentById(anyLong());

        // Verify
        verify(appointmentRepository, times(1)).findById(anyLong());
        verify(appointmentRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Delete appointment by id - Not found")
    void deleteAppointmentById_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                appointmentService.deleteAppointmentById(100L));

        // Verify
        assertEquals("Appointment Not Found. ID: 100", exception.getMessage());
        verify(appointmentRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find all appointments by doctorId - Success")
    void findAllAppointmentsByDoctorId_Success() {
        // Test data
        Doctor doctor = Doctor.builder().id(1L).build();
        List<Appointment> appointments = Arrays.asList(
                Appointment.builder().doctor(doctor).build(),
                Appointment.builder().doctor(doctor).build());

        // Mock method call
        when(appointmentRepository.findAllByDoctorId(anyLong())).thenReturn(appointments);

        List<AppointmentResponseDTO> result = appointmentService.findAllAppointmentsByDoctorId(1L);

        // Verify
        assertEquals(2, result.size());
        verify(appointmentRepository, times(1)).findAllByDoctorId(anyLong());
        verify(appointmentMapper, times(result.size())).appointmentToAppointmentResponseDto(any());
    }

    @Test
    @DisplayName("Find all appointments by patientId - Success")
    void findAllAppointmentsByPatientId_Success() {
        // Test data
        Patient patient = Patient.builder().id(1L).build();
        List<Appointment> appointments = Arrays.asList(
                Appointment.builder().patient(patient).build(),
                Appointment.builder().patient(patient).build());

        // Mock method call
        when(appointmentRepository.findAllByPatientId(anyLong())).thenReturn(appointments);

        List<AppointmentResponseDTO> result = appointmentService.findAllAppointmentsByPatientId(1L);

        // Verify
        assertEquals(2, result.size());
        verify(appointmentRepository, times(1)).findAllByPatientId(anyLong());
        verify(appointmentMapper, times(result.size())).appointmentToAppointmentResponseDto(any());
    }
}