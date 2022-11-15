package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.dto.mapper.PatientMapper;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientResponseDTO;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
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
    PatientMapper patientMapper = PatientMapper.INSTANCE;

    @InjectMocks
    PatientServiceImpl patientService;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        patientService = new PatientServiceImpl(patientRepository, patientMapper);
    }

    @Test
    @DisplayName("Find all patients - Success")
    void findAllPatients_Success() {
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

    @Test
    @DisplayName("Find patient by id - Success")
    void findPatientById_Success() {
        // Test data
        Patient patient = Patient.builder().name("Hsu Hsu").age(25L).gender(Gender.FEMALE).address("Yangon").phoneNumber("09111111111").build();
        PatientResponseDTO patientResponseDTO = PatientResponseDTO.builder().name("Hsu Hsu").age(25L).gender(Gender.FEMALE).address("Yangon").phoneNumber("09111111111").build();

        // Mock method call
        when(patientMapper.patientToPatientResponseDto(any())).thenReturn(patientResponseDTO);
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        
        PatientResponseDTO returnDto = patientService.findPatientById(anyLong());

         // Verify
        assertNotNull(returnDto);
        assertEquals(patient.getName(), returnDto.getName());
        verify(patientMapper, times(1)).patientToPatientResponseDto(any());
        verify(patientRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find patient by id - Not found")
    void findPatientById_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                patientService.findPatientById(100L));

         // Verify
        assertEquals("Patient Not Found. ID: 100", exception.getMessage());
        verify(patientRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Saving new patient - Success")
    void saveNewPatient_Success() {
        // Test data
        Patient patient = Patient.builder().name("Hsu Hsu").age(25L).gender(Gender.FEMALE).address("Yangon").phoneNumber("09111111111").build();
        PatientRequestDTO patientRequestDTO = PatientResponseDTO.builder().name("Hsu Hsu").age(25L).gender(Gender.FEMALE).address("Yangon").phoneNumber("09111111111").build();
        PatientResponseDTO patientResponseDTO = PatientResponseDTO.builder().name("Hsu Hsu").age(25L).gender(Gender.FEMALE).address("Yangon").phoneNumber("09111111111").build();

        // Mock method call
        when(patientMapper.patientRequestDtoToPatient(any())).thenReturn(patient);
        when(patientMapper.patientToPatientResponseDto(any())).thenReturn(patientResponseDTO);
        when(patientRepository.save(any())).thenReturn(patient);

        PatientResponseDTO savedDto = patientService.saveNewPatient(patientRequestDTO);

         // Verify
        assertEquals(patientResponseDTO, savedDto);
        assertEquals(patient.getName(), savedDto.getName());
        verify(patientMapper, times(1)).patientRequestDtoToPatient(any());
        verify(patientMapper, times(1)).patientToPatientResponseDto(any());
        verify(patientRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Update patient information - Success")
    void updatePatient_Success() {
        // Test data
        Patient patient = Patient.builder().name("Hsu Hsu").age(25L).gender(Gender.FEMALE).address("Yangon").phoneNumber("09111111111").build();
        PatientRequestDTO patientRequestDTO = PatientResponseDTO.builder().name("Hsu Hsu").age(25L).gender(Gender.FEMALE).address("Yangon").phoneNumber("09111111111").build();
        PatientResponseDTO patientResponseDTO = PatientResponseDTO.builder().name("Hsu Hsu").age(25L).gender(Gender.FEMALE).address("Yangon").phoneNumber("09111111111").build();

        // Mock method call
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        when(patientMapper.patientToPatientResponseDto(any())).thenReturn(patientResponseDTO);
        when(patientRepository.save(any())).thenReturn(patient);
        
        PatientResponseDTO savedDto = patientService.updatePatient(25L, patientRequestDTO);

         // Verify
        assertEquals(patientResponseDTO, savedDto);
        assertEquals(patient.getName(), savedDto.getName());
        verify(patientMapper, times(1)).patientToPatientResponseDto(any());
        verify(patientRepository, times(1)).save(any());
        verify(patientRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Update patient information - Not found")
    void updatePatient_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                patientService.updatePatient(100L, any()));

        // Verify
        assertEquals("Patient Not Found. ID: 100", exception.getMessage());
        verify(patientRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Delete patient by id - Success")
    void deletePatientById_Success() {
        // Test data
        Patient patient = Patient.builder().name("Hsu Hsu").age(25L).gender(Gender.FEMALE).address("Yangon").phoneNumber("09111111111").build();

        // Mock method call
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        patientService.deletePatientById(anyLong());

        // Verify
        verify(patientRepository, times(1)).findById(anyLong());
        verify(patientRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Delete patient by id - Not found")
    void deletePatientById_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                patientService.deletePatientById(100L));

        // Verify
        assertEquals("Patient Not Found. ID: 100", exception.getMessage());
        verify(patientRepository, times(1)).findById(anyLong());
    }
}