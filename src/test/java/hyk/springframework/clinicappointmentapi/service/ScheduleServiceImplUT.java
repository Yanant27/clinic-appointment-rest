package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.dto.mapper.ScheduleMapper;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
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
class ScheduleServiceImplUnitTest {
    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    DoctorRepository doctorRepository;

    @Mock
    ScheduleMapper scheduleMapper;

    @InjectMocks
    ScheduleServiceImpl scheduleService;

    @Test
    @DisplayName("Find all schedules - Success")
    void findAllSchedules_Success() {
        // Test data
        List<Schedule> scheduleList = Arrays.asList(
                Schedule.builder().build(),
                Schedule.builder().build());

        // Mock method call
        when(scheduleRepository.findAll()).thenReturn(scheduleList);

        List<ScheduleResponseDTO> result = scheduleService.findAllSchedules();

        // Verify
        assertEquals(2, result.size());
        verify(scheduleRepository, times(1)).findAll();
        verify(scheduleMapper, times(result.size())).scheduleToScheduleResponseDto(any());
    }

    @Test
    @DisplayName("Find schedule by id - Success")
    void findScheduleById_Success() {
        // Test data
        Schedule schedule = Schedule.builder().dayOfWeek("Monday").build();
        ScheduleResponseDTO scheduleResponseDTO = ScheduleResponseDTO.builder().dayOfWeek("Monday").build();

        // Mock method call
        when(scheduleMapper.scheduleToScheduleResponseDto(any())).thenReturn(scheduleResponseDTO);
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        ScheduleResponseDTO returnDto = scheduleService.findScheduleById(anyLong());

        // Verify
        assertNotNull(returnDto);
        assertEquals(schedule.getDayOfWeek(), returnDto.getDayOfWeek());
        verify(scheduleMapper, times(1)).scheduleToScheduleResponseDto(any());
        verify(scheduleRepository, times(1)).findById(anyLong());

    }

    @Test
    @DisplayName("Find schedule by id - Not found")
    void findScheduleById_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                scheduleService.findScheduleById(100L));

        // Verify
        assertEquals("Schedule Not Found. ID: 100", exception.getMessage());
        verify(scheduleRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Saving new schedule - Success")
    void saveNewSchedule_Success() {
        // Test data
        Schedule schedule = Schedule.builder().dayOfWeek("Monday").build();
        ScheduleResponseDTO scheduleResponseDTO = ScheduleResponseDTO.builder().dayOfWeek("Monday").build();
        ScheduleRequestDTO scheduleRequestDTO = ScheduleRequestDTO.builder().dayOfWeek("Monday").build();

        // Mock method call
        when(scheduleMapper.scheduleRequestDtoToSchedule(any())).thenReturn(schedule);
        when(scheduleMapper.scheduleToScheduleResponseDto(any())).thenReturn(scheduleResponseDTO);
        when(scheduleRepository.save(any())).thenReturn(schedule);

        ScheduleResponseDTO savedDto = scheduleService.saveNewSchedule(scheduleRequestDTO);

        // Verify
        assertEquals(scheduleResponseDTO, savedDto);
        assertEquals(schedule.getDayOfWeek(), savedDto.getDayOfWeek());
        verify(scheduleMapper, times(1)).scheduleRequestDtoToSchedule(any());
        verify(scheduleMapper, times(1)).scheduleToScheduleResponseDto(any());
        verify(scheduleRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Update schedule information - Success")
    void updateSchedule_Success() {
        // Test data
        Schedule schedule = Schedule.builder().dayOfWeek("Monday").build();
        Doctor doctor = Doctor.builder().id(2L).build();
        ScheduleResponseDTO scheduleResponseDTO = ScheduleResponseDTO.builder().id(1L).dayOfWeek("Monday").doctorId(2L).build();
        ScheduleRequestDTO scheduleRequestDTO = ScheduleRequestDTO.builder().id(1L).dayOfWeek("Monday").doctorId(2L).build();

        // Mock method call
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(scheduleMapper.scheduleToScheduleResponseDto(any())).thenReturn(scheduleResponseDTO);
        when(scheduleRepository.save(any())).thenReturn(schedule);

        ScheduleResponseDTO savedDto = scheduleService.updateSchedule(1L, scheduleRequestDTO);

        // Verify
        assertEquals(scheduleResponseDTO, savedDto);
        assertEquals(schedule.getDayOfWeek(), savedDto.getDayOfWeek());
        verify(scheduleMapper, times(1)).scheduleToScheduleResponseDto(any());
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).save(any());
        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Update schedule information - Schedule not found")
    void updateSchedule_Schedule_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                scheduleService.updateSchedule(100L, any()));

        // Verify
        assertEquals("Schedule Not Found. ID: 100", exception.getMessage());
        verify(scheduleRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Update schedule information - Doctor not found")
    void updateSchedule_Doctor_Not_Found() {
        Schedule schedule = Schedule.builder().id(1L).dayOfWeek("Monday").build();
        ScheduleRequestDTO scheduleRequestDTO = ScheduleRequestDTO.builder().id(1L).dayOfWeek("Monday").doctorId(100L).build();

        // Mock method call
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                scheduleService.updateSchedule(anyLong(), scheduleRequestDTO));

        // Verify
        assertEquals("Doctor Not Found. ID: 100", exception.getMessage());
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Delete schedule by id - Success")
    void deleteScheduleById_Success() {
        // Test data
        Schedule schedule = Schedule.builder().id(1L).dayOfWeek("Monday").build();

        // Mock method call
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        scheduleService.deleteScheduleById(anyLong());

        // Verify
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Delete schedule by id - Not found")
    void deleteScheduleById_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                scheduleService.deleteScheduleById(100L));

        // Verify
        assertEquals("Schedule Not Found. ID: 100", exception.getMessage());
        verify(scheduleRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find all schedules by doctorId - Success")
    void findAllSchedulesByDoctorId_Success() {
        // Test data
        Doctor doctor = Doctor.builder().id(1L).build();
        List<Schedule> scheduleList = Arrays.asList(
                Schedule.builder().doctor(doctor).build(),
                Schedule.builder().doctor(doctor).build());

        // Mock method call
        when(scheduleRepository.findAllByDoctorId(anyLong())).thenReturn(scheduleList);

        List<ScheduleResponseDTO> result = scheduleService.findAllSchedulesByDoctorId(anyLong());

        // Verify
        assertEquals(2, result.size());
        verify(scheduleRepository, times(1)).findAllByDoctorId(anyLong());
        verify(scheduleMapper, times(result.size())).scheduleToScheduleResponseDto(any());
    }

    @Test
    @DisplayName("Logically delete schedule by doctorId - Success")
    void logicalDeleteScheduleByDoctorId_Success() {
        // Test data
        Schedule schedule = Schedule.builder().id(1L).doctor(Doctor.builder().id(2L).build()).build();
        List<Appointment> appointments = Arrays.asList(
                Appointment.builder().id(10L).schedule(schedule).build(),
                Appointment.builder().id(11L).schedule(schedule).build());

        // Mock method call
        when(scheduleRepository.findByIdAndDoctorId(anyLong(), anyLong())).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any())).thenReturn(schedule);
        when(appointmentRepository.findAllByScheduleId(anyLong())).thenReturn(appointments);

        scheduleService.logicalDeleteScheduleByDoctorId(1L, 2L);

        // Verify
        verify(scheduleRepository, times(1)).findByIdAndDoctorId(anyLong(), anyLong());
        verify(scheduleRepository, times(1)).save(any());
        verify(appointmentRepository, times(1)).deleteAll(any());
        verify(appointmentRepository, times(1)).findAllByScheduleId(anyLong());
    }

    @Test
    @DisplayName("Logically delete schedule by doctorId - Not found")
    void logicalDeleteScheduleByDoctorId_Not_Found() {
        // Mock exception
        Exception exception = assertThrows(NotFoundException.class, () ->
                scheduleService.logicalDeleteScheduleByDoctorId(100L, 200L));

        // Verify
        assertEquals("Schedule Not Found. Schedule ID: 100, Doctor ID: 200", exception.getMessage());
        verify(scheduleRepository, times(1)).findByIdAndDoctorId(anyLong(), anyLong());
    }
}