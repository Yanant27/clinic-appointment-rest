package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import hyk.springframework.clinicappointmentapi.web.dto.ScheduleDTO;
import hyk.springframework.clinicappointmentapi.web.mapper.ScheduleMapper;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Htoo Yanant Khin
 **/
@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {
    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    DoctorRepository doctorRepository;

    @Mock
    ScheduleMapper scheduleMapper = ScheduleMapper.INSTANCE;

    @InjectMocks
    ScheduleServiceImpl scheduleService;

    List<Schedule> schedules;
    Doctor doctor;

    @BeforeEach
    public void setUp() {
        schedules = new ArrayList<>();
        doctor = Doctor.builder().name("Dr. Lin Htet").address("Mudon").phoneNumber("09123456789").specialization("Internal Medicine").build();
        schedules.add(Schedule.builder()
                .date(LocalDate.of(2022, 7, 7))
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(21, 0))
                .doctor(doctor).build());
        schedules.add(Schedule.builder()
                .date(LocalDate.of(2022, 7, 8))
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(21, 0))
                .doctor(doctor).build());
        MockitoAnnotations.openMocks(this);
        scheduleService =new ScheduleServiceImpl(scheduleRepository, doctorRepository, scheduleMapper);
    }

    @DisplayName("Display All Schedules")
    @Test
    public void findAllSchedules_success() {
        when(scheduleRepository.findAll()).thenReturn(schedules);

        List<ScheduleDTO> result = scheduleService.findAllSchedules(null);
        assertEquals(2, result.size());
        verify(scheduleRepository, times(1)).findAll();
    }

    @DisplayName("Display All Schedules - With Param - doctorId")
    @Test
    public void findAllSchedules_success_with_doctorId() {
        when(scheduleRepository.findAllByDoctorId(1L)).thenReturn(schedules);

        List<ScheduleDTO> result = scheduleService.findAllSchedules(1L);
        assertEquals(2, result.size());
        verify(scheduleRepository, times(1)).findAllByDoctorId(anyLong());
    }

    @DisplayName("Display Schedule By ID - Success")
    @Test
    public void findScheduleById_success() {
        Schedule Schedule = schedules.get(0);
        ScheduleDTO ScheduleDTO = new ScheduleDTO();

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(Schedule));
        when(scheduleMapper.scheduleToScheduleDto(any())).thenReturn(ScheduleDTO);

        ScheduleDTO returnDto = scheduleService.findScheduleById(anyLong());
        assertNotNull(returnDto);
        verify(scheduleRepository, times(1)).findById(anyLong());
    }

    @DisplayName("Display Schedule By ID - Not Found")
    @Test
    public void findScheduleById_not_found() {
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> scheduleService.findScheduleById(anyLong()));
        verify(scheduleRepository, times(1)).findById(anyLong());
        assertEquals("404 NOT_FOUND \"Schedule Not Found. ID: 0\"", exception.getMessage());
    }

    @DisplayName("Save New Schedule")
    @Test
    public void saveNewSchedule_success() {
        Schedule Schedule = schedules.get(0);
        ScheduleDTO ScheduleDTO = new ScheduleDTO();

        when(scheduleRepository.save(any())).thenReturn(Schedule);
        when(scheduleMapper.scheduleDtoToSchedule(any())).thenReturn(Schedule);
        when(scheduleMapper.scheduleToScheduleDto(any())).thenReturn(ScheduleDTO);

        ScheduleDTO returnDto = scheduleService.saveNewSchedule(any());
        assertNotNull(returnDto);
        verify(scheduleRepository, times(1)).save(any());
    }

    @DisplayName("Update Existing Schedule - Success")
    @Test
    public void updateSchedule_success() {
        Schedule schedule = schedules.get(0);
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setStartTime(schedule.getStartTime());
        scheduleDTO.setEndTime(schedule.getEndTime());
        scheduleDTO.setDoctorId(schedule.getDoctor().getId());
        scheduleDTO.setDoctorName(schedule.getDoctor().getName());

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
        when(doctorRepository.findById(scheduleDTO.getDoctorId())).thenReturn(Optional.of(doctor));
        when(scheduleRepository.save(any())).thenReturn(schedule);
        when(scheduleMapper.scheduleToScheduleDto(any())).thenReturn(scheduleDTO);

        ScheduleDTO returnDto = scheduleService.updateSchedule(anyLong(), scheduleDTO);
        assertNotNull(returnDto);
        assertEquals(scheduleDTO.getId(), returnDto.getId());
        verify(scheduleRepository, times(1)).save(any());
    }

    @DisplayName("Update Existing Schedule - Not Found")
    @Test
    public void updateSchedule_not_found() {
        ScheduleDTO ScheduleDTO = new ScheduleDTO();

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class,
                () -> scheduleService.updateSchedule(anyLong(), ScheduleDTO));
        verify(scheduleRepository, times(1)).findById(anyLong());
        assertEquals("404 NOT_FOUND \"Schedule Not Found. ID: 0\"", exception.getMessage());
    }

    @DisplayName("Delete Appointment - Success")
    @Test
    public void deleteAppointmentById_success() {
        Schedule Schedule = schedules.get(0);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(Schedule));

        scheduleService.deleteScheduleById(anyLong());
        verify(scheduleRepository, times(1)).deleteById(anyLong());
    }

    @DisplayName("Delete Appointment - Not Found")
    @Test
    public void deleteScheduleById_not_found() {
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class,
                () -> scheduleService.deleteScheduleById(anyLong()));
        verify(scheduleRepository, times(0)).deleteById(anyLong());
        assertEquals("404 NOT_FOUND \"Schedule Not Found. ID: 0\"", exception.getMessage());
    }
}