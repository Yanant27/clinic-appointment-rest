package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import hyk.springframework.clinicappointmentapi.web.dto.PatientDTO;
import hyk.springframework.clinicappointmentapi.web.mapper.PatientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Htoo Yanant Khin
 **/
@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {
    @Mock
    PatientRepository patientRepository;

    @Mock
    PatientMapper patientMapper = PatientMapper.INSTANCE;

    @InjectMocks
    PatientServiceImpl patientService;

    List<Patient> patients;

    @BeforeEach
    public void setUp() {
        patients = new ArrayList<>();
        patients.add(Patient.builder().name("Hsu Hsu").address("Yangon").phoneNumber("09111111111").build());
        patients.add(Patient.builder().name("Min Min").address("Yangon").phoneNumber("09222222222").build());

        MockitoAnnotations.openMocks(this);
        patientService =new PatientServiceImpl(patientRepository, patientMapper);
    }

    @DisplayName("Display All Patients")
    @Test
    public void findAllPatients_success() {
        when(patientRepository.findAll()).thenReturn(patients);

        List<PatientDTO> result = patientService.findAllPatients();
        assertEquals(2, result.size());
        verify(patientRepository, times(1)).findAll();
    }

    @DisplayName("Display Patient By ID - Success")
    @Test
    public void findPatientById_success() {
        Patient patient = patients.get(0);
        PatientDTO patientDTO = new PatientDTO();

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        when(patientMapper.patientToPatientDto(any())).thenReturn(patientDTO);

        PatientDTO returnDto = patientService.findPatientById(anyLong());
        assertNotNull(returnDto);
        verify(patientRepository, times(1)).findById(anyLong());
    }

    @DisplayName("Display Patient By ID - Not Found")
    @Test
    public void findPatientById_not_found() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> patientService.findPatientById(anyLong()));
        verify(patientRepository, times(1)).findById(anyLong());
        assertEquals("404 NOT_FOUND \"Patient Not Found. ID: 0\"", exception.getMessage());
    }

    @DisplayName("Save New Patient")
    @Test
    public void saveNewPatient_success() {
        Patient patient = patients.get(0);
        PatientDTO PatientDTO = new PatientDTO();

        when(patientRepository.save(any())).thenReturn(patient);
        when(patientMapper.patientDtoToPatient(any())).thenReturn(patient);
        when(patientMapper.patientToPatientDto(any())).thenReturn(PatientDTO);

        PatientDTO returnDto = patientService.saveNewPatient(any());
        assertNotNull(returnDto);
        verify(patientRepository, times(1)).save(any());
    }

    @DisplayName("Update Existing Patient - Success")
    @Test
    public void updatePatient_success() {
        Patient patient = patients.get(0);
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(patient.getId());
        patientDTO.setName(patient.getName());
        patientDTO.setAddress("Mandaly");
        patientDTO.setPhoneNumber("09222222999");

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        when(patientRepository.save(any())).thenReturn(patient);
        when(patientMapper.patientToPatientDto(any())).thenReturn(patientDTO);

        PatientDTO returnDto = patientService.updatePatient(anyLong(), patientDTO);
        assertNotNull(returnDto);
        assertEquals(patientDTO.getName(), returnDto.getName());
        assertEquals(patientDTO.getAddress(), returnDto.getAddress());
        assertEquals(patientDTO.getPhoneNumber(), returnDto.getPhoneNumber());
        verify(patientRepository, times(1)).save(any());
    }

    @DisplayName("Update Existing Patient - Not Found")
    @Test
    public void updatePatient_not_found() {
        PatientDTO PatientDTO = new PatientDTO();

        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class,
                () -> patientService.updatePatient(anyLong(), PatientDTO));
        verify(patientRepository, times(1)).findById(anyLong());
        assertEquals("404 NOT_FOUND \"Patient Not Found. ID: 0\"", exception.getMessage());
    }

    @DisplayName("Delete Appointment - Success")
    @Test
    public void deleteAppointmentById_success() {
        Patient Patient = patients.get(0);

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(Patient));

        patientService.deletePatientById(anyLong());
        verify(patientRepository, times(1)).deleteById(anyLong());
    }

    @DisplayName("Delete Appointment - Not Found")
    @Test
    public void deletePatientById_not_found() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class,
                () -> patientService.deletePatientById(anyLong()));
        verify(patientRepository, times(0)).deleteById(anyLong());
        assertEquals("404 NOT_FOUND \"Patient Not Found. ID: 0\"", exception.getMessage());
    }
}