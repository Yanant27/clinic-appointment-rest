package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.util.JsonStringUtil;
import hyk.springframework.clinicappointmentapi.web.dto.DoctorDTO;
import hyk.springframework.clinicappointmentapi.web.mapper.DoctorMapper;
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
public class DoctorControllerIT {
    private final String API_ROOT = "/api/v1/doctors";

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    DoctorMapper doctorMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @DisplayName("Display All Doctors")
    @Test
    public void showAllAppointments_without_param() throws Exception {
        mockMvc.perform(get(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @DisplayName("Display Doctor By ID")
    @Test
    public void showDoctorById_success() throws Exception {
        mockMvc.perform(get(API_ROOT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @DisplayName("Display Doctor By ID - Not Found")
    @Test
    public void showDoctorById_not_found() throws Exception {
        mockMvc.perform(get(API_ROOT + "/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Create New Doctor")
    @Test
    public void createDoctor() throws Exception {
        DoctorDTO newDto = DoctorDTO.builder()
                            .name("Dr. A")
                            .phoneNumber("09123456789")
                            .address("Yangon")
                            .degree("MBBS")
                            .specialization("Cardiology").build();

        mockMvc.perform(post(API_ROOT)
                        .content(JsonStringUtil.asJsonString(newDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @DisplayName("Update Existing Doctor - Success")
    @Test
    public void updateDoctor_success() throws Exception {
        DoctorDTO updatedDto = doctorMapper.doctorToDoctorDTto(doctorRepository.findById(1L).get());
        updatedDto.setPhoneNumber("09777777777");

        mockMvc.perform(put(API_ROOT + "/1")
                        .content(JsonStringUtil.asJsonString(updatedDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber", equalTo("09777777777")));
    }

    @DisplayName("Update Existing Doctor - Not Found")
    @Test
    public void updateDoctor_not_found() throws Exception{
        DoctorDTO updatedDto = doctorMapper.doctorToDoctorDTto(doctorRepository.findById(1L).get());
        updatedDto.setPhoneNumber("09777777777");

        mockMvc.perform(put(API_ROOT + "/99999")
                        .content(JsonStringUtil.asJsonString(updatedDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete Doctor - Success")
    @Test
    public void deleteDoctorById_success() throws Exception {
        mockMvc.perform(delete(API_ROOT + "/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @DisplayName("Delete Doctor - Not Found")
    @Test
    public void deleteDoctorById_not_found() throws Exception {
        mockMvc.perform(delete(API_ROOT + "/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
