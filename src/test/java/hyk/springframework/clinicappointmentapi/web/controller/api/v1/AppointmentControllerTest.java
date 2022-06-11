package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import hyk.springframework.clinicappointmentapi.service.AppointmentService;
import hyk.springframework.clinicappointmentapi.util.JsonStringUtil;
import hyk.springframework.clinicappointmentapi.web.dto.AppointmentDTO;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Htoo Yanant Khin
 **/
@ExtendWith(MockitoExtension.class)
public class AppointmentControllerTest {
    private final String BASE_URL = "/api/v1/appointments";

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    AppointmentController appointmentController;

    MockMvc mockMvc;

    List<AppointmentDTO> appointmentDTOs;
    
    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
        appointmentDTOs = new ArrayList<>();
        appointmentDTOs.add(AppointmentDTO.builder()
                .appointmentId(1L)
                .appointmentDate(LocalDate.of(2022,7,7))
                .appointmentStatus(AppointmentStatus.BOOKED)
                .scheduleId(3L)
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(12,0))
                .doctorId(10L)
                .doctorName("Dr. Lin Htet")
                .patientId(21L)
                .patientName("Hsu Hsu").build());
        appointmentDTOs.add(AppointmentDTO.builder()
                .appointmentId(2L)
                .appointmentDate(LocalDate.of(2022,7,8))
                .appointmentStatus(AppointmentStatus.BOOKED)
                .scheduleId(3L)
                .startTime(LocalTime.of(12,0))
                .endTime(LocalTime.of(13,0))
                .doctorId(10L)
                .doctorName("Dr. Lin Htet")
                .patientId(21L)
                .patientName("Hsu Hsu").build());
        
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController).build();
    }

    @DisplayName("Display All Appointments - without Request Parameter")
    @Test
    public void showAllAppointments_success_without_param() throws Exception {
        when(appointmentService.findAllAppointments(null, null, null)).thenReturn(appointmentDTOs);

        mockMvc.perform(get(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("Display All Appointments - With Request Param, doctorId")
    @Test
    public void showAllAppointments_success_with_doctorId() throws Exception {
        when(appointmentService.findAllAppointments(10L, null, null)).thenReturn(appointmentDTOs);

        mockMvc.perform(get(BASE_URL)
                        .param("doctorId", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("Display All Appointments - With Request Param, patientId")
    @Test
    public void showAllAppointments_success_with_patientId() throws Exception {
        when(appointmentService.findAllAppointments(null, 21L, null)).thenReturn(appointmentDTOs);

        mockMvc.perform(get(BASE_URL)
                        .param("patientId", "21")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("Display All Appointments - With Request Param, scheduleId")
    @Test
    public void showAllAppointments_success_with_scheduleId() throws Exception {
        when(appointmentService.findAllAppointments(null, null, 3L)).thenReturn(appointmentDTOs);

        mockMvc.perform(get(BASE_URL)
                        .param("scheduleId", "3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("Display Appointment By ID")
    @Test
    public void showById_success() throws Exception {
        AppointmentDTO appointmentDTO = appointmentDTOs.get(0);
        when(appointmentService.findAppointmentById(anyLong())).thenReturn(appointmentDTO);

        mockMvc.perform(get(BASE_URL + "/" + appointmentDTO.getAppointmentId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentStatus", equalTo(appointmentDTO.getAppointmentStatus().toString())))
                .andExpect(jsonPath("$.doctorName", equalTo(appointmentDTO.getDoctorName())))
                .andExpect(jsonPath("$.patientName", equalTo(appointmentDTO.getPatientName())))
                .andExpect(jsonPath("$.appointmentDate", equalTo(appointmentDTO.getAppointmentDate().toString())));
    }

    @DisplayName("Create Appointment")
    @Test
    public void createAppointment_success() throws Exception {
        AppointmentDTO appointmentDTO = appointmentDTOs.get(0);
        when(appointmentService.saveAppointment(appointmentDTO)).thenReturn(appointmentDTO);

        mockMvc.perform(post(BASE_URL)
                    .content(JsonStringUtil.asJsonString(appointmentDTO))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.appointmentStatus", equalTo("BOOKED")))
                .andExpect(jsonPath("$.doctorName", equalTo("Dr. Lin Htet")))
                .andExpect(jsonPath("$.patientName", equalTo("Hsu Hsu")))
                .andExpect(jsonPath("$.appointmentDate", equalTo("2022-07-07")));
    }

    @DisplayName("Update Appointment Status")
    @Test
    public void updateAppointment_success() throws Exception {
        AppointmentDTO appointmentDTO = appointmentDTOs.get(0);

        AppointmentDTO savedDto = appointmentDTOs.get(0);
        savedDto.setAppointmentStatus(AppointmentStatus.CANCELLED);
        when(appointmentService.findAppointmentById(anyLong())).thenReturn(appointmentDTO);
        when(appointmentService.saveAppointment(appointmentDTO)).thenReturn(savedDto);

        // when -  action or the behaviour that we are going test
        mockMvc.perform(patch(BASE_URL + "/" + savedDto.getAppointmentId())
                    .content(JsonStringUtil.asJsonString(appointmentDTO))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                // then - verify the output
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentStatus", is("CANCELLED")));
    }

    @DisplayName("Delete Appointment")
    @Test
    public void deleteAppointment_success() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().is2xxSuccessful());

        verify(appointmentService).deleteAppointmentById(anyLong());
    }
}