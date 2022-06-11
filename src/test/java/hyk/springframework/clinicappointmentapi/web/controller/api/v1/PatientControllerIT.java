package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import hyk.springframework.clinicappointmentapi.util.JsonStringUtil;
import hyk.springframework.clinicappointmentapi.web.dto.PatientDTO;
import hyk.springframework.clinicappointmentapi.web.mapper.PatientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
public class PatientControllerIT {
    private final String API_ROOT = "/api/v1/patients";

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    PatientMapper patientMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @DisplayName("Display All Patients")
    @Test
    public void showAllAppointments_without_param() throws Exception {
        mockMvc.perform(get(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @DisplayName("Display Patient By ID")
    @Test
    public void showPatientById_success() throws Exception {
        mockMvc.perform(get(API_ROOT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @DisplayName("Display Patient By ID - Not Found")
    @Test
    public void showPatientById_not_found() throws Exception {
        mockMvc.perform(get(API_ROOT + "/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Create New Patient")
    @Test
    public void createPatient() throws Exception {
        PatientDTO newDto = PatientDTO.builder()
                            .name("Hsu Hsu")
                            .phoneNumber("09123456789")
                            .address("Yangon").build();

        mockMvc.perform(post(API_ROOT)
                        .content(JsonStringUtil.asJsonString(newDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @DisplayName("Update Existing Patient - Success")
    @Test
    public void updatePatient_success() throws Exception {
        PatientDTO updatedDto = patientMapper.patientToPatientDto(patientRepository.findById(1L).get());
        updatedDto.setPhoneNumber("09777777777");

        mockMvc.perform(put(API_ROOT + "/1")
                        .content(JsonStringUtil.asJsonString(updatedDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber", equalTo("09777777777")));
    }

    @DisplayName("Update Existing Patient - Not Found")
    @Test
    public void updatePatient_not_found() throws Exception{
        PatientDTO updatedDto = patientMapper.patientToPatientDto(patientRepository.findById(1L).get());
        updatedDto.setPhoneNumber("09777777777");

        mockMvc.perform(put(API_ROOT + "/99999")
                        .content(JsonStringUtil.asJsonString(updatedDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete Patient - Success")
    @Test
    public void deletePatientById_success() throws Exception {
        mockMvc.perform(delete(API_ROOT + "/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @DisplayName("Delete Patient - Not Found")
    @Test
    public void deletePatientById_not_found() throws Exception {
        mockMvc.perform(delete(API_ROOT + "/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
