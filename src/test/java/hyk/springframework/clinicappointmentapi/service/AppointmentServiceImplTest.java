package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.web.dto.AppointmentDTO;
import hyk.springframework.clinicappointmentapi.web.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.web.mapper.AppointmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author Htoo Yanant Khin
 **/
@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {
    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    AppointmentMapper appointmentMapper = AppointmentMapper.INSTANCE;

    @InjectMocks
    AppointmentServiceImpl appointmentService;

    List<Appointment> appointments;

    @BeforeEach
    void setUp() {
        appointments = new ArrayList<>();
        Doctor doctor = Doctor.builder().name("Dr. Lin Htet").address("Mudon").phoneNumber("09123456789").specialization("Internal Medicine").build();
        doctor.setId(1L);
        Patient patient = Patient.builder().name("Hsu Hsu").address("Yangon").phoneNumber("09987654321").build();
        patient.setId(2L);
        Schedule schedule = Schedule.builder().build();
        schedule.setId(3L);
        appointments.add(Appointment.builder()
                .appointmentDate(LocalDate.of(2022,7,7))
                .appointmentStatus(AppointmentStatus.BOOKED)
                .schedule(schedule)
                .doctor(doctor)
                .patient(patient).build());
        appointments.add(Appointment.builder()
                .appointmentDate(LocalDate.of(2022,7,8))
                .appointmentStatus(AppointmentStatus.BOOKED)
                .schedule(schedule)
                .doctor(doctor)
                .patient(patient).build());

        MockitoAnnotations.openMocks(this);
        appointmentService =new AppointmentServiceImpl(appointmentRepository, appointmentMapper);
    }

    @DisplayName("Display All Appointments - Without Param")
    @Test
    public void findAllAppointments_success() {
        when(appointmentRepository.findAll()).thenReturn(appointments);

        List<AppointmentDTO> result = appointmentService.findAllAppointments(null, null, null);
        assertEquals(2, result.size());
        verify(appointmentRepository, times(1)).findAll();
    }

    @DisplayName("Display All Appointments - With Param - doctorId")
    @Test
    public void findAllAppointments_success_with_doctorId() {
        when(appointmentRepository.findAllByDoctorId(1L)).thenReturn(appointments);

        List<AppointmentDTO> result = appointmentService.findAllAppointments(1L, null, null);
        assertEquals(2, result.size());
        verify(appointmentRepository, times(1)).findAllByDoctorId(anyLong());
    }

    @DisplayName("Display All Appointments - With Param - patientId")
    @Test
    public void findAllAppointments_success_with_patientId() {
        when(appointmentRepository.findAllByPatientId(2L)).thenReturn(appointments);

        List<AppointmentDTO> result = appointmentService.findAllAppointments(null, 2L, null);
        assertEquals(2, result.size());
        verify(appointmentRepository, times(1)).findAllByPatientId(anyLong());
    }

    @DisplayName("Display All Appointments - With Param - scheduleId")
    @Test
    public void findAllAppointments_success_with_scheduleId() {
        when(appointmentRepository.findAllByScheduleId(3L)).thenReturn(appointments);

        List<AppointmentDTO> result = appointmentService.findAllAppointments(null, null, 3L);
        assertEquals(2, result.size());
        verify(appointmentRepository, times(1)).findAllByScheduleId(anyLong());
    }

    @DisplayName("Display Appointment By ID - Success")
    @Test
    public void findAppointmentById_success() {
        Appointment appointment = appointments.get(0);
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        when(appointmentMapper.appointmentToAppointmentDto(any())).thenReturn(appointmentDTO);

        AppointmentDTO returnDto = appointmentService.findAppointmentById(anyLong());
        assertNotNull(returnDto);
        verify(appointmentRepository, times(1)).findById(anyLong());
    }

    @DisplayName("Display Appointment By ID - Not Found")
    @Test
    public void findAppointmentById_not_found() {
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class,
                () -> appointmentService.findAppointmentById(anyLong()));
        verify(appointmentRepository, times(1)).findById(anyLong());
        assertEquals("Appointment Not Found. ID: 0", exception.getMessage());
    }

    @DisplayName("Save Appointment")
    @Test
    public void saveAppointment() {
        Appointment appointment = appointments.get(0);
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(appointmentMapper.appointmentDtoToAppointment(any())).thenReturn(appointment);
        when(appointmentMapper.appointmentToAppointmentDto(any())).thenReturn(appointmentDTO);

        AppointmentDTO returnDto = appointmentService.saveAppointment(any());
        assertNotNull(returnDto);
        verify(appointmentRepository, times(1)).save(any());
    }

    @DisplayName("Delete Appointment - Success")
    @Test
    public void deleteAppointmentById_success() {
        Appointment appointment = appointments.get(0);

        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        appointmentService.deleteAppointmentById(anyLong());
        verify(appointmentRepository, times(1)).deleteById(anyLong());
    }

    @DisplayName("Delete Appointment - Not Found")
    @Test
    public void deleteAppointmentById_not_found() {
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class,
                () -> appointmentService.deleteAppointmentById(anyLong()));
        verify(appointmentRepository, times(0)).deleteById(anyLong());
        assertEquals("Appointment Not Found. ID: 0", exception.getMessage());
    }
}