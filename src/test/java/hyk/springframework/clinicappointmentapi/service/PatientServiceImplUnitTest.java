package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.domain.security.Role;
import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.mapper.PatientMapper;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientUpdateDTO;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import hyk.springframework.clinicappointmentapi.exception.ResourceAlreadyExistException;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import hyk.springframework.clinicappointmentapi.repository.security.RoleRepository;
import hyk.springframework.clinicappointmentapi.repository.security.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Htoo Yanant Khin
 **/
@ExtendWith(MockitoExtension.class)
class PatientServiceImplUnitTest
{
    @Mock
    PatientRepository patientRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PatientMapper patientMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    PatientServiceImpl patientService;

    @DisplayName("Find All Patients")
    @Nested
    class ListPatients {
        @Test
        public void findAllPatients_Success() {
            // Test data
            List<Patient> patients = Arrays.asList(
                    Patient.builder().build(),
                    Patient.builder().build());

            // Mock method call
            when(patientRepository.findAll()).thenReturn(patients);

            List<PatientResponseDTO> result = patientService.findAllPatients();

            // Verify
            assertEquals(2, result.size());
            verify(patientRepository, times(1)).findAll();
            verify(patientMapper, times(result.size())).patientToPatientResponseDto(any());
        }
    }

    @DisplayName("Display Patient By ID")
    @Nested
    class FindPatientById {
        @Test
        public void findPatientById_Success() {
            // Test data
            Patient patient = Patient.builder()
                    .id(1L)
                    .name("Hsu Hsu")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon")
                    .appointments(List.of(
                            Appointment.builder().id(1L).build(),
                            Appointment.builder().id(2L).build()))
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            PatientResponseDTO patientResponseDTO = PatientResponseDTO.builder()
                    .id(1L)
                    .name("Hsu Hsu")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon")
                    .appointments(List.of(
                            AppointmentResponseDTO.builder().id(1L).build(),
                            AppointmentResponseDTO.builder().id(2L).build()))
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(patientMapper.patientToPatientResponseDto(any())).thenReturn(patientResponseDTO);
            when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

            PatientResponseDTO returnDto = patientService.findPatientById(anyLong());

            // Verify
            assertNotNull(returnDto);
            assertEquals(1L, returnDto.getId());
            assertEquals("Hsu Hsu", returnDto.getName());
            assertEquals(LocalDate.of(1998, 1,1), returnDto.getDateOfBirth());
            assertEquals(Gender.FEMALE, returnDto.getGender());
            assertEquals("09222222222", returnDto.getPhoneNumber());
            assertEquals("No.30, Bo Ta Htaung Township, Yangon", returnDto.getAddress());
            assertEquals(2, returnDto.getAppointments().size());
            assertEquals("admin", returnDto.getCreatedBy());
            assertEquals("admin", returnDto.getModifiedBy());
            verify(patientMapper, times(1)).patientToPatientResponseDto(any());
            verify(patientRepository, times(1)).findById(anyLong());
        }

        @Test
        public void findPatientById_Not_Found() {
            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    patientService.findPatientById(99999L));

            // Verify
            assertEquals("Patient Not Found. ID: 99999", exception.getMessage());
            verify(patientRepository, times(1)).findById(anyLong());
        }
    }

    @DisplayName("Register New Patient")
    @Nested
    class RegisterPatient {
        @Test
        public void saveNewPatient_Success() {
            // Test data
            Patient patient = Patient.builder()
                    .id(1L)
                    .name("Hsu Hsu")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon")
                    .createdBy("hsuhsu")
                    .modifiedBy("hsuhsu")
                    .build();
            PatientRegistrationDTO patientUpdateDTO = PatientRegistrationDTO.builder()
                    .name("Hsu Hsu")
                    .username("hsuhsu")
                    .password("password")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon")
                    .build();
            PatientResponseDTO patientResponseDTO = PatientResponseDTO.builder()
                    .id(1L)
                    .name("Hsu Hsu")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon")
                    .createdBy("hsuhsu")
                    .modifiedBy("hsuhsu")
                    .build();
            Role rolePatient = Role.builder().name("PATIENT").build();

            // Mock method call
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
            when(roleRepository.findByNameEqualsIgnoreCase(anyString())).thenReturn(Optional.of(rolePatient));
            when(passwordEncoder.encode(anyString())).thenReturn("fdfsffweffsfsdfnsdferwhtlgnlelwkntwertjwo");
            when(patientMapper.patientRegistrationDtoToPatient(any())).thenReturn(patient);
            when(patientMapper.patientToPatientResponseDto(any())).thenReturn(patientResponseDTO);
            when(patientRepository.save(any())).thenReturn(patient);

            PatientResponseDTO savedDto = patientService.saveNewPatient(patientUpdateDTO);

            // Verify
            assertEquals(1L, savedDto.getId());
            assertEquals("Hsu Hsu", savedDto.getName());
            assertEquals(LocalDate.of(1998, 1,1), savedDto.getDateOfBirth());
            assertEquals(Gender.FEMALE, savedDto.getGender());
            assertEquals("09222222222", savedDto.getPhoneNumber()); // updated phone number
            assertEquals("No.30, Bo Ta Htaung Township, Yangon", savedDto.getAddress()); // updated address
            assertEquals("hsuhsu", savedDto.getCreatedBy());
            assertEquals("hsuhsu", savedDto.getModifiedBy());
            verify(userRepository, times(1)).findByUsername(anyString());
            verify(roleRepository, times(1)).findByNameEqualsIgnoreCase(anyString());
            verify(passwordEncoder, times(1)).encode(anyString());
            verify(patientMapper, times(1)).patientRegistrationDtoToPatient(any());
            verify(patientMapper, times(1)).patientToPatientResponseDto(any());
            verify(patientRepository, times(1)).save(any());
        }

        @Test
        void saveNewPatient_Fail_Username_Existed() {
            // Test data
            User user = User.builder()
                    .id(1L)
                    .username("hsuhsu")
                    .password("hsuhsu")
                    .role(Role.builder().name("PATIENT").build())
                    .build();
            PatientRegistrationDTO patientRegistrationDTO = PatientRegistrationDTO.builder()
                    .name("Hsu Hsu")
                    .username("hsuhsu")
                    .password("password")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon")
                    .build();

            // Mock method call
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

            // Mock exception
            Exception exception = assertThrows(ResourceAlreadyExistException.class, () ->
                    patientService.saveNewPatient(patientRegistrationDTO));

            // Verify
            assertEquals("Username already exists !", exception.getMessage());
            verify(userRepository, times(1)).findByUsername(anyString());
        }
    }

    @DisplayName("Update Patient")
    @Nested
    class UpdatePatient {
        @Test
        public void updatePatient_Success() {
            // Test data
            Patient patient = Patient.builder()
                    .id(1L)
                    .name("Hsu Hsu")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon")
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            PatientUpdateDTO patientUpdateDTO = PatientUpdateDTO.builder()
                    .id(1L)
                    .name("Hsu Hsu")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09111111111") // updated phone number
                    .address("Mandalay") // updated address
                    .build();
            PatientResponseDTO patientResponseDTO = PatientResponseDTO.builder()
                    .id(1L)
                    .name("Hsu Hsu")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09111111111") // updated phone number
                    .address("Mandalay") // updated address
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
            when(patientMapper.patientToPatientResponseDto(any())).thenReturn(patientResponseDTO);
            when(patientRepository.save(any())).thenReturn(patient);

            PatientResponseDTO savedDto = patientService.updatePatient(25L, patientUpdateDTO);

            // Verify
            assertEquals(1L, savedDto.getId());
            assertEquals("Hsu Hsu", savedDto.getName());
            assertEquals(LocalDate.of(1998, 1,1), savedDto.getDateOfBirth());
            assertEquals(Gender.FEMALE, savedDto.getGender());
            assertEquals("09111111111", savedDto.getPhoneNumber()); // updated phone number
            assertEquals("Mandalay", savedDto.getAddress()); // updated address
            assertEquals("admin", savedDto.getCreatedBy());
            assertEquals("admin", savedDto.getModifiedBy());
            verify(patientRepository, times(1)).findById(anyLong());
            verify(patientMapper, times(1)).patientToPatientResponseDto(any());
            verify(patientRepository, times(1)).save(any());
        }

        @Test
        public void updatePatient_Not_Found() {
            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    patientService.updatePatient(99999L, any()));

            // Verify
            assertEquals("Patient Not Found. ID: 99999", exception.getMessage());
            verify(patientRepository, times(1)).findById(anyLong());
        }
    }

    @DisplayName("Delete Patient")
    @Nested
    class DeletePatient {
        @Test
        public void deletePatientById_Success() {
            // Test data
            Patient patient = Patient.builder()
                    .id(1L)
                    .name("Hsu Hsu")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon")
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

            patientService.deletePatientById(anyLong());

            // Verify
            verify(patientRepository, times(1)).findById(anyLong());
            verify(patientRepository, times(1)).deleteById(anyLong());
        }

        @Test
        public void deletePatientById_Not_Found() {
            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    patientService.deletePatientById(99999L));

            // Verify
            assertEquals("Patient Not Found. ID: 99999", exception.getMessage());
            verify(patientRepository, times(1)).findById(anyLong());
        }  
    }
}
