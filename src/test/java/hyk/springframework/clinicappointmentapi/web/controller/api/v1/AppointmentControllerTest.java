package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.service.AppointmentService;
import hyk.springframework.clinicappointmentapi.util.JsonStringUtil;
import hyk.springframework.clinicappointmentapi.web.dto.AppointmentDTO;
import hyk.springframework.clinicappointmentapi.web.dto.DoctorDTO;
import hyk.springframework.clinicappointmentapi.web.dto.PatientDTO;
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
                .appointmentStatus("BOOKED")
                .scheduleId(3L)
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(12,0))
                .doctorDTO(DoctorDTO.builder().id(10L).name("Dr. Lin Htet").address("Mudon").phoneNumber("09123456789").specialization("Internal Medicine").build())
                .patientDTO(PatientDTO.builder().id(21L).name("Hsu Hsu").address("Yangon").phoneNumber("09987654321").build()).build());
        appointmentDTOs.add(AppointmentDTO.builder()
                .appointmentId(2L)
                .appointmentDate(LocalDate.of(2022,7,8))
                .appointmentStatus("BOOKED")
                .scheduleId(4L)
                .startTime(LocalTime.of(12,0))
                .endTime(LocalTime.of(13,0))
                .doctorDTO(DoctorDTO.builder().id(11L).name("Dr. Nay Oo").address("Yangon").phoneNumber("09123456789").specialization("Internal Medicine").build())
                .patientDTO(PatientDTO.builder().id(21L).name("Min Min").address("Yangon").phoneNumber("09987654321").build()).build());
        
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController).build();
    }

    @DisplayName("Display All Appointments")
    @Test
    public void showAllAppointments_success() throws Exception {
        when(appointmentService.findAllAppointments()).thenReturn(appointmentDTOs);

        mockMvc.perform(get(BASE_URL)
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
                .andExpect(jsonPath("$.appointmentStatus", equalTo(appointmentDTO.getAppointmentStatus())))
                .andExpect(jsonPath("$.doctorDTO.name", equalTo(appointmentDTO.getDoctorDTO().getName())))
                .andExpect(jsonPath("$.patientDTO.name", equalTo(appointmentDTO.getPatientDTO().getName())))
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
                .andExpect(jsonPath("$.doctorDTO.name", equalTo("Dr. Lin Htet")))
                .andExpect(jsonPath("$.patientDTO.name", equalTo("Hsu Hsu")))
                .andExpect(jsonPath("$.appointmentDate", equalTo("2022-07-07")));
    }

    @DisplayName("Update Appointment Status")
    @Test
    public void updateAppointment_success() throws Exception {
        AppointmentDTO appointmentDTO = appointmentDTOs.get(0);

        AppointmentDTO savedDto = appointmentDTOs.get(0);
        savedDto.setAppointmentStatus("CANCELLED");

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