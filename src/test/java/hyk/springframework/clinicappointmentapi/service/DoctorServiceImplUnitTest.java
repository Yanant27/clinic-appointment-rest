package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.domain.security.Role;
import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorUpdateDTO;
import hyk.springframework.clinicappointmentapi.dto.mapper.DoctorMapper;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import hyk.springframework.clinicappointmentapi.exception.ResourceAlreadyExistException;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
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
import static org.mockito.Mockito.*;

/**
 * @author Htoo Yanant Khin
 **/
@ExtendWith(MockitoExtension.class)
class DoctorServiceImplUnitTest {
    @Mock
    DoctorRepository doctorRepository;
    
    @Mock
    UserRepository userRepository;
    
    @Mock
    RoleRepository roleRepository;

    @Mock
    DoctorMapper doctorMapper;
    
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    DoctorServiceImpl doctorService;

    @DisplayName("Find All Doctors")
    @Nested
    class ListDoctors {
        @Test
        void findAllDoctors_Success() {
            // Test data
            List<Doctor> doctors = Arrays.asList(
                    Doctor.builder().build(),
                    Doctor.builder().build());

            // Mock method call
            when(doctorRepository.findAll()).thenReturn(doctors);

            List<DoctorResponseDTO> result = doctorService.findAllDoctors();

            // Verify
            assertEquals(2, result.size());
            verify(doctorRepository, times(1)).findAll();
            verify(doctorMapper, times(result.size())).doctorToDoctorResponseDto(any());
        }
    }

    @DisplayName("Find All Doctors By Specialization")
    @Nested
    class ListDoctorsBySpecialization {
        @Test
        void findAllDoctorsBySpecialization() {
            // Test data
            List<Doctor> doctors = Arrays.asList(
                    Doctor.builder().specialization("Cardiology").build(),
                    Doctor.builder().specialization("Cardiology").build());

            // Mock method call
            when(doctorRepository.findAllBySpecializationEqualsIgnoreCase(anyString())).thenReturn(doctors);

            List<DoctorResponseDTO> result = doctorService.findAllDoctorsBySpecialization("Cardiology");

            // Verify
            assertEquals(2, result.size());
            verify(doctorRepository, times(1)).findAllBySpecializationEqualsIgnoreCase(anyString());
            verify(doctorMapper, times(result.size())).doctorToDoctorResponseDto(any());
        }
    }

    @DisplayName("Display Doctor By ID")
    @Nested
    class FindDoctorsById {
        @Test
        void findDoctorById_Success() {
            // Test data
            Doctor doctor = Doctor.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine")
                    .appointments(List.of(
                            Appointment.builder().id(1L).build(),
                            Appointment.builder().id(2L).build()))
                    .schedules(List.of(
                            Schedule.builder().id(1L).build(),
                            Schedule.builder().id(1L).build()))
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            DoctorResponseDTO doctorResponseDTO = DoctorResponseDTO.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine")
                    .appointmentResponseDTOS(List.of(
                            AppointmentResponseDTO.builder().id(1L).build(),
                            AppointmentResponseDTO.builder().id(2L).build()))
                    .scheduleResponseDTOS(List.of(
                            ScheduleResponseDTO.builder().id(1L).build(),
                            ScheduleResponseDTO.builder().id(1L).build()))
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
            when(doctorMapper.doctorToDoctorResponseDto(any())).thenReturn(doctorResponseDTO);

            DoctorResponseDTO returnDto = doctorService.findDoctorById(anyLong());

            // Verify
            assertNotNull(returnDto);
            assertEquals(1L, returnDto.getId());
            assertEquals("Dr. Lin Htet", returnDto.getName());
            assertEquals(LocalDate.of(1990,1,1), returnDto.getDateOfBirth());
            assertEquals(Gender.MALE, returnDto.getGender());
            assertEquals("09111111111", returnDto.getPhoneNumber());
            assertEquals("No.1, 1st street, Than Lyin", returnDto.getAddress());
            assertEquals("Internal medicine", returnDto.getSpecialization());
            assertEquals("MBBS", returnDto.getQualifications());
            assertEquals(2, returnDto.getAppointmentResponseDTOS().size());
            assertEquals(2, returnDto.getScheduleResponseDTOS().size());
            assertEquals("admin", returnDto.getCreatedBy());
            assertEquals("admin", returnDto.getModifiedBy());
            verify(doctorMapper, times(1)).doctorToDoctorResponseDto(any());
            verify(doctorRepository, times(1)).findById(anyLong());
        }

        @Test
        @DisplayName("Find doctor by id - Not found")
        void findDoctorById_Not_Found() {
            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    doctorService.findDoctorById(99999L));

            // Verify
            assertEquals("Doctor Not Found. ID: 99999", exception.getMessage());
            verify(doctorRepository, times(1)).findById(anyLong());
        }
    }

    @DisplayName("Create New Doctor")
    @Nested
    class SaveNewDoctor {
        @Test
        void saveNewDoctor_Success() {
            // Test data
            Doctor doctor = Doctor.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine")
                    .appointments(List.of(
                            Appointment.builder().id(1L).build(),
                            Appointment.builder().id(2L).build()))
                    .schedules(List.of(
                            Schedule.builder().id(1L).build(),
                            Schedule.builder().id(1L).build()))
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            DoctorRegistrationDTO doctorRegistrationDTO = DoctorRegistrationDTO.builder()
                    .name("Dr. Lin Htet")
                    .username("linhtet")
                    .password("password")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine")
                    .build();
            DoctorResponseDTO doctorResponseDTO = DoctorResponseDTO.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine")
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            Role roleDoctor = Role.builder().name("DOCTOR").build();
            
            // Mock method call
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
            when(roleRepository.findByNameEqualsIgnoreCase(anyString())).thenReturn(Optional.of(roleDoctor));
            when(passwordEncoder.encode(anyString())).thenReturn("fdfsffweffsfsdfnsdferwhtlgnlelwkntwertjwo");
            when(doctorMapper.doctorRegistrationDtoToDoctor(any())).thenReturn(doctor);
            when(doctorMapper.doctorToDoctorResponseDto(any())).thenReturn(doctorResponseDTO);
            when(doctorRepository.save(any())).thenReturn(doctor);

            DoctorResponseDTO savedDto = doctorService.saveNewDoctor(doctorRegistrationDTO);

            // Verify
            assertEquals(1L, savedDto.getId());
            assertEquals("Dr. Lin Htet", savedDto.getName());
            assertEquals(LocalDate.of(1990,1,1), savedDto.getDateOfBirth());
            assertEquals(Gender.MALE, savedDto.getGender());
            assertEquals("09111111111", savedDto.getPhoneNumber());
            assertEquals("No.1, 1st street, Than Lyin", savedDto.getAddress());
            assertEquals("Internal medicine", savedDto.getSpecialization());
            assertEquals("MBBS", savedDto.getQualifications());
            assertEquals("admin", savedDto.getCreatedBy());
            assertEquals("admin", savedDto.getModifiedBy());
            verify(doctorMapper, times(1)).doctorRegistrationDtoToDoctor(any());
            verify(doctorMapper, times(1)).doctorToDoctorResponseDto(any());
            verify(doctorRepository, times(1)).save(any());
        }

        @Test
        void saveNewDoctor_Fail_Username_Existed() {
            // Test data
            User user = User.builder()
                    .id(1L)
                    .username("linhtet")
                    .password("linhtet")
                    .role(Role.builder().name("DOCTOR").build())
                    .build();
            DoctorRegistrationDTO doctorRegistrationDTO = DoctorRegistrationDTO.builder()
                    .name("Dr. Lin Htet")
                    .username("linhtet")
                    .password("password")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine")
                    .build();

            // Mock method call
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

            // Mock exception
            Exception exception = assertThrows(ResourceAlreadyExistException.class, () ->
                    doctorService.saveNewDoctor(doctorRegistrationDTO));

            // Verify
            assertEquals("Username already exists !", exception.getMessage());
            verify(userRepository, times(1)).findByUsername(anyString());
        }
    }

    @DisplayName("Update Doctor")
    @Nested
    class UpdateDoctor {
        @Test
        void updateDoctor_Success() {
            // Test data
            Doctor doctor = Doctor.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine")
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            DoctorUpdateDTO doctorUpdateDTO = DoctorUpdateDTO.builder()
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09999999999") // update phone number
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Cardiology") // update specialization
                    .build();
            DoctorResponseDTO doctorResponseDTO = DoctorResponseDTO.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09999999999") // update phone number
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Cardiology") // update specialization
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
            when(doctorMapper.doctorToDoctorResponseDto(any())).thenReturn(doctorResponseDTO);
            when(doctorRepository.save(any())).thenReturn(doctor);

            DoctorResponseDTO savedDto = doctorService.updateDoctor(1L, doctorUpdateDTO);

            // Verify
            assertEquals(1L, savedDto.getId());
            assertEquals("Dr. Lin Htet", savedDto.getName());
            assertEquals(LocalDate.of(1990,1,1), savedDto.getDateOfBirth());
            assertEquals(Gender.MALE, savedDto.getGender());
            assertEquals("09999999999", savedDto.getPhoneNumber()); // update phone number
            assertEquals("No.1, 1st street, Than Lyin", savedDto.getAddress());
            assertEquals("Cardiology", savedDto.getSpecialization()); // update specialization
            assertEquals("MBBS", savedDto.getQualifications());
            assertEquals("admin", savedDto.getCreatedBy());
            assertEquals("admin", savedDto.getModifiedBy());
            verify(doctorMapper, times(1)).doctorToDoctorResponseDto(any());
            verify(doctorRepository, times(1)).findById(anyLong());
            verify(doctorRepository, times(1)).save(any());
            verify(doctorRepository, times(1)).findById(anyLong());
        }

        @Test
        void updateDoctor_Not_Found() {
            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    doctorService.updateDoctor(99999L, any()));

            // Verify
            assertEquals("Doctor Not Found. ID: 99999", exception.getMessage());
            verify(doctorRepository, times(1)).findById(anyLong());
        }
    }

    @DisplayName("Delete Doctor")
    @Nested
    class DeleteDoctor {
        @Test
        void deleteDoctorById_Success() {
            // Test data
            Doctor doctor = Doctor.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine")
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

            doctorService.deleteDoctorById(1L);

            // Verify
            verify(doctorRepository, times(1)).findById(anyLong());
            verify(doctorRepository, times(1)).deleteById(anyLong());
        }

        @Test
        void deleteDoctorById_Not_Found() {
            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    doctorService.deleteDoctorById(99999L));

            // Verify
            assertEquals("Doctor Not Found. ID: 99999", exception.getMessage());
            verify(doctorRepository, times(1)).findById(anyLong());
        }
    }
}