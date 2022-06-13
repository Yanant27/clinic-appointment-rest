package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.web.dto.DoctorDTO;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.web.mapper.DoctorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Htoo Yanant Khin
 **/
@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {
    @Mock
    DoctorRepository doctorRepository;

    @Mock
    DoctorMapper doctorMapper = DoctorMapper.INSTANCE;

    @InjectMocks
    DoctorServiceImpl doctorService;

    List<Doctor> doctors;

    @BeforeEach
    public void setUp() {
        doctors = new ArrayList<>();
        doctors.add(Doctor.builder().name("Dr. Lin Htet").address("Mudon").phoneNumber("09123456789").specialization("Internal Medicine").build());
        doctors.add(Doctor.builder().name("Dr. Nay Oo").address("Yangon").phoneNumber("09123456789").specialization("Internal Medicine").build());

        MockitoAnnotations.openMocks(this);
        doctorService =new DoctorServiceImpl(doctorRepository, doctorMapper);
    }

    @DisplayName("Display All Doctors")
    @Test
    public void findAllDoctors_success() {
        when(doctorRepository.findAll()).thenReturn(doctors);

        List<DoctorDTO> result = doctorService.findAllDoctors();
        assertEquals(2, result.size());
        verify(doctorRepository, times(1)).findAll();
    }

    @DisplayName("Display Doctor By ID - Success")
    @Test
    public void findDoctorById_success() {
        Doctor doctor = doctors.get(0);
        DoctorDTO doctorDTO = new DoctorDTO();

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(doctorMapper.doctorToDoctorDTto(any())).thenReturn(doctorDTO);

        DoctorDTO returnDto = doctorService.findDoctorById(anyLong());
        assertNotNull(returnDto);
        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @DisplayName("Display Doctor By ID - Not Found")
    @Test
    public void findDoctorById_not_found() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class,
                () -> doctorService.findDoctorById(anyLong()));
        verify(doctorRepository, times(1)).findById(anyLong());
        assertEquals("Doctor Not Found. ID: 0", exception.getMessage());
    }

    @DisplayName("Save New Doctor")
    @Test
    public void saveNewDoctor_success() {
        Doctor doctor = doctors.get(0);
        DoctorDTO doctorDTO = new DoctorDTO();

        when(doctorRepository.save(any())).thenReturn(doctor);
        when(doctorMapper.doctorDtoToDoctor(any())).thenReturn(doctor);
        when(doctorMapper.doctorToDoctorDTto(any())).thenReturn(doctorDTO);

        DoctorDTO returnDto = doctorService.saveNewDoctor(any());
        assertNotNull(returnDto);
        verify(doctorRepository, times(1)).save(any());
    }

    @DisplayName("Update Existing Doctor - Success")
    @Test
    public void updateDoctor_success() {
        Doctor doctor = doctors.get(0);
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(doctor.getId());
        doctorDTO.setName(doctor.getName());
        doctorDTO.setAddress(doctor.getAddress());
        doctorDTO.setPhoneNumber("09222222222");
        doctorDTO.setDegree(doctor.getDegree());
        doctorDTO.setSpecialization("Cardiology");

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any())).thenReturn(doctor);
        when(doctorMapper.doctorToDoctorDTto(any())).thenReturn(doctorDTO);

        DoctorDTO returnDto = doctorService.updateDoctor(anyLong(), doctorDTO);
        assertNotNull(returnDto);
        assertEquals(doctorDTO.getName(), returnDto.getName());
        assertEquals(doctorDTO.getAddress(), returnDto.getAddress());
        assertEquals(doctorDTO.getPhoneNumber(), returnDto.getPhoneNumber());
        assertEquals(doctorDTO.getDegree(), returnDto.getDegree());
        assertEquals(doctorDTO.getSpecialization(), returnDto.getSpecialization());
        verify(doctorRepository, times(1)).save(any());
    }

    @DisplayName("Update Existing Doctor - Not Found")
    @Test
    public void updateDoctor_not_found() {
        DoctorDTO doctorDTO = new DoctorDTO();

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class,
                () -> doctorService.updateDoctor(anyLong(), doctorDTO));
        verify(doctorRepository, times(1)).findById(anyLong());
        assertEquals("Doctor Not Found. ID: 0", exception.getMessage());
    }

    @DisplayName("Delete Appointment - Success")
    @Test
    public void deleteAppointmentById_success() {
        Doctor doctor = doctors.get(0);

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        doctorService.deleteDoctorById(anyLong());
        verify(doctorRepository, times(1)).deleteById(anyLong());
    }

    @DisplayName("Delete Appointment - Not Found")
    @Test
    public void deleteDoctorById_not_found() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class,
                () -> doctorService.deleteDoctorById(anyLong()));
        verify(doctorRepository, times(0)).deleteById(anyLong());
        assertEquals("Doctor Not Found. ID: 0", exception.getMessage());
    }
}