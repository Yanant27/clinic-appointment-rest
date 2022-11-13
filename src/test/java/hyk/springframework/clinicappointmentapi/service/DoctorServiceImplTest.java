package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.mapper.DoctorMapper;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class DoctorServiceImplTest {
    @Mock
    DoctorRepository doctorRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    DoctorMapper doctorMapper;

    @InjectMocks
    DoctorServiceImpl doctorService;

    @Test
    @DisplayName("Find all doctors - Success")
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

    @Test
    @DisplayName("Find doctor by id - Success")
    void findDoctorById_Success() {
        // Test data
        Doctor doctor = Doctor.builder().name("Dr. Lin Htet").build();
        DoctorResponseDTO doctorResponseDTO = DoctorResponseDTO.builder().name("Dr. Lin Htet").build();

        // Mock method call
        when(doctorMapper.doctorToDoctorResponseDto(any())).thenReturn(doctorResponseDTO);
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        DoctorResponseDTO returnDto = doctorService.findDoctorById(anyLong());

        // Verify
        assertNotNull(returnDto);
        assertEquals(doctor.getName(), returnDto.getName());
        verify(doctorMapper, times(1)).doctorToDoctorResponseDto(any());
        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find doctor by id - Not found")
    void findDoctorById_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                doctorService.findDoctorById(100L));

        // Verify
        assertEquals("Doctor Not Found. ID: 100", exception.getMessage());
        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Saving new doctor - Success")
    void saveNewDoctor() {
        // Test data
        Doctor doctor = Doctor.builder().name("Dr. Lin Htet").build();
        DoctorRequestDTO doctorRequestDTO = DoctorRequestDTO.builder().name("Dr. Lin Htet").build();
        DoctorResponseDTO doctorResponseDTO = DoctorResponseDTO.builder().name("Dr. Lin Htet").build();

        // Mock method call
        when(doctorMapper.doctorRequestDtoToDoctor(any())).thenReturn(doctor);
        when(doctorMapper.doctorToDoctorResponseDto(any())).thenReturn(doctorResponseDTO);
        when(doctorRepository.save(any())).thenReturn(doctor);

        DoctorResponseDTO savedDto = doctorService.saveNewDoctor(doctorRequestDTO);

        // Verify
        assertEquals(doctorResponseDTO, savedDto);
        assertEquals(doctor.getName(), savedDto.getName());
        verify(doctorMapper, times(1)).doctorRequestDtoToDoctor(any());
        verify(doctorMapper, times(1)).doctorToDoctorResponseDto(any());
        verify(doctorRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Update doctor information - Success")
    void updateDoctor_Success() {
        // Test data
        Doctor doctor = Doctor.builder().id(1L).name("Dr. Lin Htet").build();
        DoctorRequestDTO doctorRequestDTO = DoctorRequestDTO.builder().id(1L).name("Dr. Lin Htet").build();
        DoctorResponseDTO doctorResponseDTO = DoctorResponseDTO.builder().id(1L).name("Dr. Lin Htet").build();

        // Mock method call
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(doctorMapper.doctorToDoctorResponseDto(any())).thenReturn(doctorResponseDTO);
        when(doctorRepository.save(any())).thenReturn(doctor);

        DoctorResponseDTO savedDto = doctorService.updateDoctor(1L, doctorRequestDTO);

        // Verify
        assertEquals(doctorResponseDTO, savedDto);
        assertEquals(doctor.getName(), savedDto.getName());
        verify(doctorMapper, times(1)).doctorToDoctorResponseDto(any());
        verify(doctorRepository, times(1)).findById(anyLong());
        verify(doctorRepository, times(1)).save(any());
        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Update doctor information - Not found")
    void updateDoctor_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                doctorService.updateDoctor(100L, any()));

        // Verify
        assertEquals("Doctor Not Found. ID: 100", exception.getMessage());
        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Delete schedule by id - Success")
    void deleteDoctorById_Success() {
        // Test data
        Doctor doctor = Doctor.builder().id(1L).name("Dr. Lin Htet").build();
        List<Schedule> schedules = Arrays.asList(
                Schedule.builder().doctor(doctor).build(),
                Schedule.builder().doctor(doctor).build());

        // Mock method call
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(scheduleRepository.findAllByDoctorId(anyLong())).thenReturn(schedules);

        doctorService.deleteDoctorById(1L);

        // Verify
        verify(doctorRepository, times(1)).findById(anyLong());
        verify(doctorRepository, times(1)).deleteById(anyLong());
        verify(scheduleRepository, times(1)).findAllByDoctorId(anyLong());
    }

    @Test
    @DisplayName("Delete schedule by id - Not found")
    void deleteDoctorById_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                doctorService.deleteDoctorById(100L));

        // Verify
        assertEquals("Doctor Not Found. ID: 100", exception.getMessage());
        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find all doctors by specialization - Success")
    void findAllDoctorsBySpecialization() {
        // Test data
        List<Doctor> doctors = Arrays.asList(
                Doctor.builder().specialization("Cardiology").build(),
                Doctor.builder().specialization("Cardiology").build());

        // Mock method call
        when(doctorRepository.findAllBySpecialization(anyString())).thenReturn(doctors);

        List<DoctorResponseDTO> result = doctorService.findAllDoctorsBySpecialization("Cardiology");

        // Verify
        assertEquals(2, result.size());
        verify(doctorRepository, times(1)).findAllBySpecialization(anyString());
        verify(doctorMapper, times(result.size())).doctorToDoctorResponseDto(any());
    }
}