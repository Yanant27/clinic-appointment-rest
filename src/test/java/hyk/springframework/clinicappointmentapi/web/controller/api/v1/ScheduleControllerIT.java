package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.enums.DoctorStatus;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import hyk.springframework.clinicappointmentapi.util.JsonStringUtil;
import hyk.springframework.clinicappointmentapi.web.dto.ScheduleDTO;
import hyk.springframework.clinicappointmentapi.web.mapper.ScheduleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Htoo Yanant Khin
 **/
@SpringBootTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
public class ScheduleControllerIT {
    private final String API_ROOT = "/api/v1/schedules";

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleMapper scheduleMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
    @DisplayName("Display All Schedules - Without Request Parameter")
    @Test
    public void showAllSchedules_without_param() throws Exception {
        mockMvc.perform(get(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
    @DisplayName("Display All Schedules - With Request Param, doctorId")
    @Test
    public void showAllSchedules_with_doctorId() throws Exception {
        mockMvc.perform(get(API_ROOT)
                        .param("doctorId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
    @DisplayName("Display Schedule By ID")
    @Test
    public void showScheduleById_success() throws Exception {
        mockMvc.perform(get(API_ROOT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
    @DisplayName("Display Schedule By ID - Not Found")
    @Test
    public void showScheduleById_not_found() throws Exception {
        mockMvc.perform(get(API_ROOT + "/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR"})
    @DisplayName("Create New Schedule - Success")
    @Test
    public void createSchedule_success() throws Exception {
        ScheduleDTO newDto = ScheduleDTO.builder()
                .date(LocalDate.of(2022, 8, 8))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(10, 0))
                .doctorId(1L)
                .doctorName("Dr. Lin Htet")
                .doctorStatus(DoctorStatus.AVAILABLE).build();
        mockMvc.perform(post(API_ROOT)
                        .content(JsonStringUtil.asJsonString(newDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", notNullValue()));
    }

        @WithMockUser(roles = {"PATIENT"})
    @DisplayName("Create New Schedule - Access Denied")
    @Test
    public void createSchedule_denied() throws Exception {
        ScheduleDTO newDto = ScheduleDTO.builder()
                .date(LocalDate.of(2022, 8, 8))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(10, 0))
                .doctorId(1L)
                .doctorName("Dr. Lin Htet")
                .doctorStatus(DoctorStatus.AVAILABLE).build();
        mockMvc.perform(post(API_ROOT)
                        .content(JsonStringUtil.asJsonString(newDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR"})
    @DisplayName("Update Existing Schedule - Success")
    @Test
    public void updateSchedule_success() throws Exception {
        ScheduleDTO updatedDto = scheduleMapper.scheduleToScheduleDto(scheduleRepository.findById(1L).get());
        updatedDto.setDoctorStatus(DoctorStatus.UNAVAILABLE);

        mockMvc.perform(put(API_ROOT + "/1")
                        .content(JsonStringUtil.asJsonString(updatedDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorStatus", equalTo("UNAVAILABLE")));
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR"})
    @DisplayName("Update Existing Schedule - Not Found")
    @Test
    public void updateSchedule_not_found() throws Exception{
        ScheduleDTO updatedDto = scheduleMapper.scheduleToScheduleDto(scheduleRepository.findById(1L).get());
        updatedDto.setDoctorStatus(DoctorStatus.UNAVAILABLE);

        mockMvc.perform(put(API_ROOT + "/99999")
                        .content(JsonStringUtil.asJsonString(updatedDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = {"PATIENT"})
    @DisplayName("Update Existing Schedule - Access Denied")
    @Test
    public void updateSchedule_denied() throws Exception{
        ScheduleDTO updatedDto = scheduleMapper.scheduleToScheduleDto(scheduleRepository.findById(1L).get());
        updatedDto.setDoctorStatus(DoctorStatus.UNAVAILABLE);

        mockMvc.perform(put(API_ROOT + "/99999")
                        .content(JsonStringUtil.asJsonString(updatedDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR"})
    @DisplayName("Delete Schedule - Success")
    @Test
    public void deleteScheduleById_success() throws Exception {
        mockMvc.perform(delete(API_ROOT + "/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR"})
    @DisplayName("Delete Schedule - Not Found")
    @Test
    public void deleteScheduleById_not_found() throws Exception {
        mockMvc.perform(delete(API_ROOT + "/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = {"PATIENT"})
    @DisplayName("Delete Schedule - Access Denied")
    @Test
    public void deleteScheduleById_denied() throws Exception {
        mockMvc.perform(delete(API_ROOT + "/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
