package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.service.DoctorService;
import hyk.springframework.clinicappointmentapi.util.JsonStringUtil;
import hyk.springframework.clinicappointmentapi.web.dto.DoctorDTO;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Htoo Yanant Khin
 **/
@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {
    private final String BASE_URL = "/api/v1/doctors";

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    MockMvc mockMvc;

    List<DoctorDTO> doctorDTOs;

    @BeforeEach
    public void setUp() {
        doctorDTOs = new ArrayList<>();
        doctorDTOs.add(DoctorDTO.builder().id(10L).name("Dr. Lin Htet").address("Mudon").phoneNumber("09123456789").degree("MBBS").specialization("Internal Medicine").build());
        doctorDTOs.add(DoctorDTO.builder().id(11L).name("Dr. Nay Oo").address("Yangon").phoneNumber("09123456789").degree("MBBS").specialization("Internal Medicine").build());
        mockMvc = MockMvcBuilders.standaloneSetup(doctorController).build();
    }

    @DisplayName("Display All Doctors")
    @Test
    public void showAllAppointments() throws Exception {
        when(doctorService.findAllDoctors()).thenReturn(doctorDTOs);

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(doctorService, times(1)).findAllDoctors();
    }

    @DisplayName("Display Doctor By ID")
    @Test
    public void showDoctorById_success() throws Exception {
        DoctorDTO doctorDTO = doctorDTOs.get(0);
        when(doctorService.findDoctorById(anyLong())).thenReturn(doctorDTO);

        mockMvc.perform(get(BASE_URL + "/" + doctorDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(doctorDTO.getName())))
                .andExpect(jsonPath("$.address", equalTo(doctorDTO.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", equalTo(doctorDTO.getPhoneNumber())))
                .andExpect(jsonPath("$.degree", equalTo(doctorDTO.getDegree())))
                .andExpect(jsonPath("$.specialization", equalTo(doctorDTO.getSpecialization())));
        verify(doctorService, times(1)).findDoctorById(anyLong());
    }

    @DisplayName("Create Doctor")
    @Test
    public void createDoctor() throws Exception {
        DoctorDTO doctorDTO = doctorDTOs.get(0);
        when(doctorService.saveNewDoctor(doctorDTO)).thenReturn(doctorDTO);

        mockMvc.perform(post(BASE_URL)
                        .content(JsonStringUtil.asJsonString(doctorDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", equalTo(doctorDTO.getName())))
                .andExpect(jsonPath("$.address", equalTo(doctorDTO.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", equalTo(doctorDTO.getPhoneNumber())))
                .andExpect(jsonPath("$.degree", equalTo(doctorDTO.getDegree())))
                .andExpect(jsonPath("$.specialization", equalTo(doctorDTO.getSpecialization())));
        verify(doctorService, times(1)).saveNewDoctor(any());
    }

    @Test
    public void updateDoctor() throws Exception {
        DoctorDTO doctorDTO = doctorDTOs.get(0);

        DoctorDTO savedDto = doctorDTOs.get(0);
        savedDto.setPhoneNumber("09888888888");

        when(doctorService.updateDoctor(doctorDTO.getId(), doctorDTO)).thenReturn(savedDto);

        // when -  action or the behaviour that we are going test
        mockMvc.perform(put(BASE_URL + "/" + savedDto.getId())
                        .content(JsonStringUtil.asJsonString(doctorDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                // then - verify the output
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber", is(savedDto.getPhoneNumber())));
        verify(doctorService, times(1)).updateDoctor(anyLong(), any());
    }

    @DisplayName("Delete Doctor")
    @Test
    public void deleteAppointment() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(doctorService).deleteDoctorById(anyLong());
    }
}