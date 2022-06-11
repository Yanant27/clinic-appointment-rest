package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.service.PatientService;
import hyk.springframework.clinicappointmentapi.util.JsonStringUtil;
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
class PatientControllerUT {
    private final String API_ROOT = "/api/v1/patients";

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    MockMvc mockMvc;

    List<PatientDTO> patientDTOs;

    @BeforeEach
    public void setUp() {
        patientDTOs = new ArrayList<>();
        patientDTOs.add(PatientDTO.builder().id(10L).name("Hsu Hsu").address("Yangon").phoneNumber("09111111111").build());
        patientDTOs.add(PatientDTO.builder().id(11L).name("Min Min").address("Yangon").phoneNumber("09222222222").build());
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
    }

    @DisplayName("Display All Patients")
    @Test
    public void showAllAppointments() throws Exception {
        when(patientService.findAllPatients()).thenReturn(patientDTOs);

        mockMvc.perform(get(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @DisplayName("Display Patient By ID")
    @Test
    public void showPatientById_success() throws Exception {
        PatientDTO PatientDTO = patientDTOs.get(0);
        when(patientService.findPatientById(anyLong())).thenReturn(PatientDTO);

        mockMvc.perform(get(API_ROOT + "/" + PatientDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(PatientDTO.getName())))
                .andExpect(jsonPath("$.address", equalTo(PatientDTO.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", equalTo(PatientDTO.getPhoneNumber())));
    }

    @DisplayName("Create Patient")
    @Test
    public void createPatient() throws Exception {
        PatientDTO PatientDTO = patientDTOs.get(0);
        when(patientService.saveNewPatient(PatientDTO)).thenReturn(PatientDTO);

        mockMvc.perform(post(API_ROOT)
                        .content(JsonStringUtil.asJsonString(PatientDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", equalTo(PatientDTO.getName())))
                .andExpect(jsonPath("$.address", equalTo(PatientDTO.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", equalTo(PatientDTO.getPhoneNumber())));
    }

    @Test
    public void updatePatient() throws Exception {
        PatientDTO PatientDTO = patientDTOs.get(0);

        PatientDTO savedDto = patientDTOs.get(0);
        savedDto.setPhoneNumber("09888888888");

        when(patientService.updatePatient(PatientDTO.getId(), PatientDTO)).thenReturn(savedDto);

        // when -  action or the behaviour that we are going test
        mockMvc.perform(put(API_ROOT + "/" + savedDto.getId())
                        .content(JsonStringUtil.asJsonString(PatientDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                // then - verify the output
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber", is(savedDto.getPhoneNumber())));
    }

    @DisplayName("Delete Patient")
    @Test
    public void deleteAppointment() throws Exception {
        mockMvc.perform(delete(API_ROOT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(patientService).deletePatientById(anyLong());
    }
}