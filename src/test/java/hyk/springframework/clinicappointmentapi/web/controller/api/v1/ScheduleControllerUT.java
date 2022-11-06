package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.enums.DoctorStatus;
import hyk.springframework.clinicappointmentapi.service.ScheduleService;
import hyk.springframework.clinicappointmentapi.util.JsonStringUtil;
import hyk.springframework.clinicappointmentapi.web.dto.ScheduleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Htoo Yanant Khin
 **/
@ExtendWith(MockitoExtension.class)
class ScheduleControllerUT {
    private final String API_ROOT = "/api/v1/schedules";

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    MockMvc mockMvc;

    List<ScheduleDTO> scheduleDTOs;

    @BeforeEach
    public void setUp() {
        scheduleDTOs = new ArrayList<>();
        scheduleDTOs.add(ScheduleDTO.builder()
                .id(10L)
                .date(LocalDate.of(2022, 12, 12))
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(21, 0))
                .doctorId(1L)
                .doctorName("Dr. Min Thet")
                .doctorStatus(DoctorStatus.AVAILABLE).build());
        scheduleDTOs.add(ScheduleDTO.builder()
                .id(11L)
                .date(LocalDate.of(2022, 7, 8))
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(21, 0))
                .doctorId(1L)
                .doctorName("Dr. Min Thet")
                .doctorStatus(DoctorStatus.AVAILABLE).build());
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
    }

    @DisplayName("Display All Schedules - Without Request Parameter")
    @Test
    public void showAllAppointments_without_param() throws Exception {
        when(scheduleService.findAllSchedules(null)).thenReturn(scheduleDTOs);

        mockMvc.perform(get(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(scheduleService, times(1)).findAllSchedules(null);
    }

    @DisplayName("Display All Schedules - With Request Param, doctorId")
    @Test
    public void showAllAppointments_with_doctorId() throws Exception {
        when(scheduleService.findAllSchedules(1L)).thenReturn(scheduleDTOs);

        mockMvc.perform(get(API_ROOT)
                        .param("doctorId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(scheduleService, times(1)).findAllSchedules(anyLong());
    }

    @DisplayName("Display Schedule By ID")
    @Test
    public void showScheduleById_success() throws Exception {
        ScheduleDTO scheduleDTO = scheduleDTOs.get(0);
        when(scheduleService.findScheduleById(anyLong())).thenReturn(scheduleDTO);

        mockMvc.perform(get(API_ROOT + "/" + scheduleDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", equalTo(scheduleDTO.getDate().toString())))
                .andExpect(jsonPath("$.startTime", equalTo(scheduleDTO.getStartTime().toString())))
                .andExpect(jsonPath("$.endTime", equalTo(scheduleDTO.getEndTime().toString())))
                .andExpect(jsonPath("$.doctorName", equalTo(scheduleDTO.getDoctorName())))
                .andExpect(jsonPath("$.doctorStatus", equalTo(scheduleDTO.getDoctorStatus().toString())));
    }

    @DisplayName("Create Schedule")
    @Test
    public void createSchedule() throws Exception {
        ScheduleDTO scheduleDTO = scheduleDTOs.get(0);
        when(scheduleService.saveNewSchedule(scheduleDTO)).thenReturn(scheduleDTO);

        mockMvc.perform(post(API_ROOT)
                        .content(JsonStringUtil.asJsonString(scheduleDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", equalTo(10)))
                .andExpect(jsonPath("$.date", equalTo(scheduleDTO.getDate().toString())))
                .andExpect(jsonPath("$.startTime", equalTo(scheduleDTO.getStartTime().toString())))
                .andExpect(jsonPath("$.endTime", equalTo(scheduleDTO.getEndTime().toString())))
                .andExpect(jsonPath("$.doctorId", equalTo(1)))
                .andExpect(jsonPath("$.doctorName", equalTo(scheduleDTO.getDoctorName())))
                .andExpect(jsonPath("$.doctorStatus", equalTo(scheduleDTO.getDoctorStatus().toString())));
    }

    @DisplayName("Update Schedule")
    @Test
    public void updateSchedule() throws Exception {
        ScheduleDTO scheduleDTO = scheduleDTOs.get(0);

        ScheduleDTO savedDto = scheduleDTOs.get(0);
        savedDto.setDate(LocalDate.of(2022,12,12));

        when(scheduleService.updateSchedule(scheduleDTO.getId(), scheduleDTO)).thenReturn(savedDto);

        // when -  action or the behaviour that we are going test
        mockMvc.perform(put(API_ROOT + "/" + savedDto.getId())
                        .content(JsonStringUtil.asJsonString(scheduleDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                // then - verify the output
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", equalTo(scheduleDTO.getDate().toString())));
    }

    @DisplayName("Delete Schedule")
    @Test
    public void deleteAppointment() throws Exception {
        mockMvc.perform(delete(API_ROOT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(scheduleService).deleteScheduleById(anyLong());
    }
}