package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.util.JsonStringUtil;
import hyk.springframework.clinicappointmentapi.web.dto.AppointmentDTO;
import hyk.springframework.clinicappointmentapi.web.mapper.AppointmentMapper;
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
public class AppointmentControllerIT {
    private final String API_ROOT = "/api/v1/appointments";

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    AppointmentMapper appointmentMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Display All Appointments - Without Request Parameter - Success")
    @Test
    public void showAllAppointments_without_param() throws Exception {
        mockMvc.perform(get(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
    @DisplayName("Display All Appointments - With doctorId - Success")
    @Test
    public void showAllAppointments_with_doctorId_success() throws Exception {
        mockMvc.perform(get(API_ROOT)
                        .param("doctorId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
    @DisplayName("Display All Appointments - With Request Param, patientId")
    @Test
    public void showAllAppointments_with_patientId() throws Exception {
        mockMvc.perform(get(API_ROOT)
                        .param("patientId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
    @DisplayName("Display All Appointments - With Request Param, scheduleId")
    @Test
    public void showAllAppointments_with_scheduleId() throws Exception {
        mockMvc.perform(get(API_ROOT)
                        .param("scheduleId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
    @DisplayName("Display Appointment By ID")
    @Test
    public void showAppointmentById_success() throws Exception {
        mockMvc.perform(get(API_ROOT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
    @DisplayName("Display Appointment By ID - Not Found")
    @Test
    public void showAppointmentById_not_found() throws Exception {
        mockMvc.perform(get(API_ROOT + "/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = {"ADMIN", "PATIENT"})
    @DisplayName("Create New Appointment - Success")
    @Test
    public void createAppointment_success() throws Exception {
        AppointmentDTO newDto = AppointmentDTO.builder()
                .appointmentDate(LocalDate.of(2022, 8, 8))
                .appointmentStatus(AppointmentStatus.BOOKED)
                .scheduleId(1L)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(10, 0))
                .doctorId(1L)
                .doctorName("Dr. Lin Htet")
                .patientId(1L)
                .patientName("Hsu Hsu").build();
        mockMvc.perform(post(API_ROOT)
                        .content(JsonStringUtil.asJsonString(newDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @WithMockUser(roles = {"DOCTOR"})
    @DisplayName("Create New Appointment - Access Denied")
    @Test
    public void createAppointment_denied() throws Exception {
        AppointmentDTO newDto = AppointmentDTO.builder()
                .appointmentDate(LocalDate.of(2022, 8, 8))
                .appointmentStatus(AppointmentStatus.BOOKED)
                .scheduleId(1L)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(10, 0))
                .doctorId(1L)
                .doctorName("Dr. Lin Htet")
                .patientId(1L)
                .patientName("Hsu Hsu").build();
        mockMvc.perform(post(API_ROOT)
                        .content(JsonStringUtil.asJsonString(newDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR"})
    @DisplayName("Update Existing Appointment - Success")
    @Test
    public void updateAppointment_success() throws Exception {
        AppointmentDTO updatedDto = appointmentMapper.appointmentToAppointmentDto(appointmentRepository.findById(1L).get());
        updatedDto.setAppointmentStatus(AppointmentStatus.CANCELLED);

        mockMvc.perform(patch(API_ROOT + "/1")
                        .content(JsonStringUtil.asJsonString(updatedDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentStatus", equalTo("CANCELLED")));
    }

    @WithMockUser(roles = {"ADMIN", "DOCTOR"})
    @DisplayName("Update Existing Appointment - Not Found")
    @Test
    public void updateAppointment_not_found() throws Exception{
        AppointmentDTO updatedDto = appointmentMapper.appointmentToAppointmentDto(appointmentRepository.findById(1L).get());
        updatedDto.setAppointmentStatus(AppointmentStatus.COMPLETED);

        mockMvc.perform(patch(API_ROOT + "/99999")
                        .content(JsonStringUtil.asJsonString(updatedDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = {"PATIENT"})
    @DisplayName("Update Existing Appointment - Access Denied")
    @Test
    public void updateAppointment_denied() throws Exception {
        AppointmentDTO updatedDto = appointmentMapper.appointmentToAppointmentDto(appointmentRepository.findById(1L).get());
        updatedDto.setAppointmentStatus(AppointmentStatus.CANCELLED);

        mockMvc.perform(patch(API_ROOT + "/1")
                        .content(JsonStringUtil.asJsonString(updatedDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Delete Appointment - Success")
    @Test
    public void deleteAppointmentById_success() throws Exception {
        mockMvc.perform(delete(API_ROOT + "/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Delete Appointment - Not Found")
    @Test
    public void deleteAppointmentById_not_found() throws Exception {
        mockMvc.perform(delete(API_ROOT + "/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles = {"PATIENT", "DOCTOR"})
    @DisplayName("Delete Appointment - Access Denied")
    @Test
    public void deleteAppointmentById_denied() throws Exception {
        mockMvc.perform(delete(API_ROOT + "/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
