package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.dto.mapper.ScheduleMapper;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleUpdateDTO;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
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
class ScheduleServiceImplUnitTest {
    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    DoctorRepository doctorRepository;

    @Mock
    ScheduleMapper scheduleMapper;

    @InjectMocks
    ScheduleServiceImpl scheduleService;

    @DisplayName("Find All Schedules")
    @Nested
    class ListSchedules {
        @Test
        public void findAllSchedules_Success() {
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
    }

    @DisplayName("Find All Schedules by Doctor ID")
    @Nested
    class ListSchedulesByDoctorId {
        @Test
        public void findAllSchedulesByDoctorId_Success() {
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
    }

    @DisplayName("Find Schedule By ID")
    @Nested
    class FindScheduleById {
        @Test
        public void findScheduleById_Success() {
            // Test data
            Schedule schedule = Schedule.builder()
                    .id(1L)
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("09:00 ~ 10:00")
                    .doctor(Doctor.builder().name("Dr. Lin Htet").build())
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            ScheduleResponseDTO scheduleResponseDTO = ScheduleResponseDTO.builder()
                    .id(1L)
                    .doctorId(1L)
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("09:00 ~ 10:00")
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(scheduleMapper.scheduleToScheduleResponseDto(any())).thenReturn(scheduleResponseDTO);
            when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

            ScheduleResponseDTO returnDto = scheduleService.findScheduleById(anyLong());

            // Verify
            assertNotNull(returnDto);
            assertEquals("SUNDAY", returnDto.getDayOfWeek());
            assertEquals(1L, returnDto.getId());
            assertEquals(1L, returnDto.getDoctorId());
            assertEquals("09:00 ~ 10:00", returnDto.getTimeslot());
            assertEquals("admin", returnDto.getCreatedBy());
            assertEquals("admin", returnDto.getModifiedBy());
            verify(scheduleMapper, times(1)).scheduleToScheduleResponseDto(any());
            verify(scheduleRepository, times(1)).findById(anyLong());
        }

        @Test
        public void findScheduleById_Not_Found() {
            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    scheduleService.findScheduleById(99999L));

            // Verify
            assertEquals("Schedule Not Found. ID: 99999", exception.getMessage());
            verify(scheduleRepository, times(1)).findById(anyLong());
        }
    }


    @DisplayName("Create New Schedule")
    @Nested
    class SaveNewSchedule {
        @Test
        public void saveNewSchedule_Success() {
            // Test data
            Schedule schedule = Schedule.builder()
                    .id(1L)
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("09:00 ~ 10:00")
                    .doctor(Doctor.builder().name("Dr. Lin Htet").build())
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            ScheduleRegistrationDTO scheduleRegistrationDTO = ScheduleRegistrationDTO.builder()
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("09:00 ~ 10:00")
                    .build();
            ScheduleResponseDTO scheduleResponseDTO = ScheduleResponseDTO.builder()
                    .id(1L)
                    .doctorId(1L)
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("09:00 ~ 10:00")
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(scheduleMapper.scheduleRegistrationDtoToSchedule(any())).thenReturn(schedule);
            when(scheduleMapper.scheduleToScheduleResponseDto(any())).thenReturn(scheduleResponseDTO);
            when(scheduleRepository.save(any())).thenReturn(schedule);

            ScheduleResponseDTO savedDto = scheduleService.saveNewSchedule(scheduleRegistrationDTO);

            // Verify
            assertNotNull(savedDto);
            assertEquals(1L, savedDto.getId());
            assertEquals(1L, savedDto.getDoctorId());
            assertEquals("SUNDAY", savedDto.getDayOfWeek());
            assertEquals("09:00 ~ 10:00", savedDto.getTimeslot());
            assertEquals("admin", savedDto.getCreatedBy());
            assertEquals("admin", savedDto.getModifiedBy());
            verify(scheduleMapper, times(1)).scheduleRegistrationDtoToSchedule(any());
            verify(scheduleMapper, times(1)).scheduleToScheduleResponseDto(any());
            verify(scheduleRepository, times(1)).save(any());
        }
    }

    @DisplayName("Update Schedule")
    @Nested
    class UpdateSchedule {
        @Test
        public void updateSchedule_Success() {
            // Test data
            Schedule schedule = Schedule.builder()
                    .id(1L)
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("09:00 ~ 10:00")
                    .doctor(Doctor.builder().name("Dr. Lin Htet").build())
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            ScheduleUpdateDTO scheduleUpdateDTO = ScheduleUpdateDTO.builder()
                    .id(1L)
                    .doctorId(1L)
                    .dayOfWeek(DayOfWeek.MONDAY.name()) // change day of week
                    .timeslot("14:00 ~ 15:00") // change timeslot
                    .build();
            ScheduleResponseDTO scheduleResponseDTO = ScheduleResponseDTO.builder()
                    .id(1L)
                    .doctorId(1L)
                    .dayOfWeek(DayOfWeek.MONDAY.name()) // change day of week
                    .timeslot("14:00 ~ 15:00") // change timeslot
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            Doctor doctor = Doctor.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
            when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
//            when(LoginUserUtil.getLoginUsername()).thenReturn("admin");
            when(scheduleMapper.scheduleToScheduleResponseDto(any())).thenReturn(scheduleResponseDTO);
            when(scheduleRepository.save(any())).thenReturn(schedule);

            ScheduleResponseDTO updatedDto = scheduleService.updateSchedule(1L, scheduleUpdateDTO);

            // Verify
            assertNotNull(updatedDto);
            assertEquals(1L, updatedDto.getId());
            assertEquals(1L, updatedDto.getDoctorId());
            assertEquals("MONDAY", updatedDto.getDayOfWeek()); // updated day of week
            assertEquals("14:00 ~ 15:00", updatedDto.getTimeslot()); // updated timeslot
            assertEquals("admin", updatedDto.getCreatedBy());
            assertEquals("admin", updatedDto.getModifiedBy());
            verify(scheduleRepository, times(1)).findById(anyLong());
            verify(doctorRepository, times(1)).findById(anyLong());
            verify(scheduleRepository, times(1)).save(any());
            verify(scheduleMapper, times(1)).scheduleToScheduleResponseDto(any());
        }

        @Test
        public void updateSchedule_Schedule_Not_Found() {
            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    scheduleService.updateSchedule(99999L, any()));

            // Verify
            assertEquals("Schedule Not Found. ID: 99999", exception.getMessage());
            verify(scheduleRepository, times(1)).findById(anyLong());
        }

        @Test
        public void updateSchedule_Doctor_Not_Found() {
            Schedule schedule = Schedule.builder()
                    .id(1L)
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("09:00 ~ 10:00")
                    .doctor(Doctor.builder().name("Dr. Lin Htet").build())
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();
            ScheduleResponseDTO scheduleResponseDTO = ScheduleResponseDTO.builder()
                    .id(1L)
                    .doctorId(99999L)
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("09:00 ~ 10:00")
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    scheduleService.updateSchedule(anyLong(), scheduleResponseDTO));

            // Verify
            assertEquals("Doctor Not Found. ID: 99999", exception.getMessage());
            verify(scheduleRepository, times(1)).findById(anyLong());
            verify(doctorRepository, times(1)).findById(anyLong());
        }
    }

    @DisplayName("Delete Schedule")
    @Nested
    class DeleteSchedule {
        @Test
        public void deleteScheduleById_Success() {
            // Test data
            Schedule schedule = Schedule.builder()
                    .id(1L)
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("09:00 ~ 10:00")
                    .doctor(Doctor.builder().name("Dr. Lin Htet").build())
                    .createdBy("admin")
                    .modifiedBy("admin")
                    .build();

            // Mock method call
            when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

            scheduleService.deleteScheduleById(anyLong());

            // Verify
            verify(scheduleRepository, times(1)).findById(anyLong());
            verify(scheduleRepository, times(1)).deleteById(anyLong());
        }

        @Test
        public void deleteScheduleById_Not_Found() {
            // Mock exception
            Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                    scheduleService.deleteScheduleById(99999L));

            // Verify
            assertEquals("Schedule Not Found. ID: 99999", exception.getMessage());
            verify(scheduleRepository, times(1)).findById(anyLong());
        }
    }
}
