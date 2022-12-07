package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentUpdateStatusDTO;
import hyk.springframework.clinicappointmentapi.dto.mapper.AppointmentMapper;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Htoo Yanant Khin
 **/
@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplUnitTest {
    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    AppointmentMapper appointmentMapper;

    @InjectMocks
    AppointmentServiceImpl appointmentService;

    @DisplayName("Find All Appointments")
    @Nested
    class ListAppointments {
        @Test
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
    }

    @DisplayName("Find All Appointments by Doctor ID")
    @Nested
    class ListAppointmentsByDoctorId {
        @Test
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
    }

    @DisplayName("Find All Appointments By Patient ID")
    @Nested
    class ListAppointmentsByPatientId {
        @Test
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

    @DisplayName("Display Appointment By ID")
    @Nested
    class FindAppointmentById {
        @Test
        void findAppointmentById_Success() {
            // Test data
            Appointment appointment = Appointment.builder()
                    .id(1L)
                    .doctor(Doctor.builder().build())
                    .patient(Patient.builder().build())
                    .timeslot("09:00 ~ 10:00")
                    .appointmentDate(LocalDate.now().plusDays(3))
                    .appointmentStatus(AppointmentStatus.PENDING)
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            AppointmentResponseDTO appointmentResponseDTO = AppointmentResponseDTO.builder()
                    .id(1L)
                    .doctorId(1L)
                    .doctorName("Dr. Lin Htet")
                    .specialization("Internal medicine")
                    .patientId(1L)
                    .patientName("Hsu Hsu")
                    .patientPhoneNumber("09222222222")
                    .timeslot("09:00 ~ 10:00")
                    .appointmentDate(LocalDate.now().plusDays(3))
                    .appointmentStatus(AppointmentStatus.PENDING)
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
            when(appointmentMapper.appointmentToAppointmentResponseDto(any())).thenReturn(appointmentResponseDTO);
            
            AppointmentResponseDTO returnDto = appointmentService.findAppointmentById(anyLong());

            // Verify
            assertEquals(1L, returnDto.getId());
            assertEquals(1L, returnDto.getDoctorId());
            assertEquals("Dr. Lin Htet", returnDto.getDoctorName());
            assertEquals("Internal medicine", returnDto.getSpecialization());
            assertEquals(1L, returnDto.getPatientId());
            assertEquals("Hsu Hsu", returnDto.getPatientName());
            assertEquals("09222222222", returnDto.getPatientPhoneNumber());
            assertEquals("09:00 ~ 10:00", returnDto.getTimeslot());
            assertEquals(appointment.getAppointmentDate(), returnDto.getAppointmentDate());
            assertEquals(AppointmentStatus.PENDING, returnDto.getAppointmentStatus());
            verify(appointmentRepository, times(1)).findById(anyLong());
            verify(appointmentMapper, times(1)).appointmentToAppointmentResponseDto(any());
        }

        @Test
        void findAppointmentById_Not_Found() {
            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    appointmentService.findAppointmentById(99999L));

            // Verify
            assertEquals("Appointment Not Found. ID: 99999", exception.getMessage());
            verify(appointmentRepository, times(1)).findById(anyLong());
        }
    }

    @DisplayName("Register New Appointment")
    @Nested
    class RegisterAppointment {
        @Test
        void saveAppointment_Old_Patient() {
            // Test data
            Appointment appointment = Appointment.builder()
                    .id(1L)
                    .doctor(Doctor.builder().build())
                    .patient(Patient.builder().build())
                    .timeslot("09:00 ~ 10:00")
                    .appointmentDate(LocalDate.now().plusDays(3))
                    .appointmentStatus(AppointmentStatus.PENDING)
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            AppointmentRegistrationDTO appointmentRegistrationDTO = AppointmentRegistrationDTO.builder()
                    .doctorId(1L)
                    .patientId(1L)
                    .timeslot("09:00 ~ 10:00")
                    .appointmentDate(LocalDate.now().plusDays(3))
                    .build();
            AppointmentResponseDTO appointmentResponseDTO = AppointmentResponseDTO.builder()
                    .id(1L)
                    .doctorId(1L)
                    .doctorName("Dr. Lin Htet")
                    .specialization("Internal medicine")
                    .patientId(1L)
                    .patientName("Hsu Hsu")
                    .patientPhoneNumber("09222222222")
                    .timeslot("09:00 ~ 10:00")
                    .appointmentDate(LocalDate.now().plusDays(3))
                    .appointmentStatus(AppointmentStatus.PENDING)
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(appointmentMapper.appointmentRegistrationDtoToAppointment(any())).thenReturn(appointment);
            when(appointmentMapper.appointmentToAppointmentResponseDto(any())).thenReturn(appointmentResponseDTO);
            when(appointmentRepository.save(any())).thenReturn(appointment);

            AppointmentResponseDTO savedDto = appointmentService.saveNewAppointment(appointmentRegistrationDTO);

            // Verify
            assertEquals(1L, savedDto.getId());
            assertEquals(1L, savedDto.getDoctorId());
            assertEquals("Dr. Lin Htet", savedDto.getDoctorName());
            assertEquals("Internal medicine", savedDto.getSpecialization());
            assertEquals(1L, savedDto.getPatientId());
            assertEquals("Hsu Hsu", savedDto.getPatientName());
            assertEquals("09222222222", savedDto.getPatientPhoneNumber());
            assertEquals("09:00 ~ 10:00", savedDto.getTimeslot());
            assertEquals(appointment.getAppointmentDate(), savedDto.getAppointmentDate());
            assertEquals(AppointmentStatus.PENDING, savedDto.getAppointmentStatus());
            verify(appointmentRepository, times(1)).save(any());
            verify(appointmentMapper, times(1)).appointmentRegistrationDtoToAppointment(any());
            verify(appointmentMapper, times(1)).appointmentToAppointmentResponseDto(any());
        }
    }

    @DisplayName("Update Appointment")
    @Nested
    class UpdateAppointment {
        @Test
        void updateAppointmentStatus_Success() {
            // Test data
            Appointment appointment = Appointment.builder()
                    .id(1L)
                    .doctor(Doctor.builder().build())
                    .patient(Patient.builder().build())
                    .timeslot("09:00 ~ 10:00")
                    .appointmentDate(LocalDate.now().plusDays(3))
                    .appointmentStatus(AppointmentStatus.PENDING)
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            AppointmentUpdateStatusDTO appointmentUpdateStatusDTO = AppointmentUpdateStatusDTO.builder()
                    .id(1L)
                    .appointmentStatus(AppointmentStatus.APPROVED)
                    .build();
            AppointmentResponseDTO appointmentResponseDTO = AppointmentResponseDTO.builder()
                    .id(1L)
                    .doctorId(1L)
                    .doctorName("Dr. Lin Htet")
                    .specialization("Internal medicine")
                    .patientId(1L)
                    .patientName("Hsu Hsu")
                    .patientPhoneNumber("09222222222")
                    .timeslot("09:00 ~ 10:00")
                    .appointmentDate(LocalDate.now().plusDays(3))
                    .appointmentStatus(AppointmentStatus.APPROVED)
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            
            // Mock method call
            when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
            when(appointmentMapper.appointmentToAppointmentResponseDto(any())).thenReturn(appointmentResponseDTO);
            when(appointmentRepository.save(any())).thenReturn(appointment);

            AppointmentResponseDTO savedDto = appointmentService.updateAppointmentStatus(1L, appointmentUpdateStatusDTO);

            // Verify
            assertEquals(1L, savedDto.getId());
            assertEquals(1L, savedDto.getDoctorId());
            assertEquals("Dr. Lin Htet", savedDto.getDoctorName());
            assertEquals("Internal medicine", savedDto.getSpecialization());
            assertEquals(1L, savedDto.getPatientId());
            assertEquals("Hsu Hsu", savedDto.getPatientName());
            assertEquals("09222222222", savedDto.getPatientPhoneNumber());
            assertEquals("09:00 ~ 10:00", savedDto.getTimeslot());
            assertEquals(appointment.getAppointmentDate(), savedDto.getAppointmentDate());
            assertEquals(AppointmentStatus.APPROVED, savedDto.getAppointmentStatus());
            verify(appointmentMapper, times(1)).appointmentToAppointmentResponseDto(any());
            verify(appointmentRepository, times(1)).save(any());
            verify(appointmentRepository, times(1)).findById(anyLong());
        }

        @Test
        void updateAppointmentStatus_Not_Found() {
            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    appointmentService.updateAppointmentStatus(99999L, any()));

            // Verify
            assertEquals("Appointment Not Found. ID: 99999", exception.getMessage());
            verify(appointmentRepository, times(1)).findById(anyLong());
        }
    }

    @DisplayName("Delete Appointment By ID")
    @Nested
    class DeleteAppointmentById {
        @Test
        void deleteAppointmentById_Success() {
            // Test data
            Appointment appointment = Appointment.builder()
                    .id(1L)
                    .doctor(Doctor.builder().build())
                    .patient(Patient.builder().build())
                    .timeslot("09:00 ~ 10:00")
                    .appointmentDate(LocalDate.now().plusDays(3))
                    .appointmentStatus(AppointmentStatus.PENDING)
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

            appointmentService.deleteAppointmentById(anyLong());

            // Verify
            verify(appointmentRepository, times(1)).findById(anyLong());
            verify(appointmentRepository, times(1)).deleteById(anyLong());
        }

        @Test
        void deleteAppointmentById_Not_Found() {
            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    appointmentService.deleteAppointmentById(99999L));

            // Verify
            assertEquals("Appointment Not Found. ID: 99999", exception.getMessage());
            verify(appointmentRepository, times(1)).findById(anyLong());
        }
    }
}
