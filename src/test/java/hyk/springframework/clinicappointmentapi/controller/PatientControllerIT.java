package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.dto.patient.PatientResponseDTO;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import hyk.springframework.clinicappointmentapi.util.JsonStringUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Htoo Yanant Khin
 **/
@SpringBootTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
//@TestPropertySource("/application-dev.properties")
public class PatientControllerIT {
    private final String API_ROOT = "/api/v1/patients";

    private final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;

    @Autowired
    private MockMvc mockMvc;


    @DisplayName("Find All Patients")
    @Nested
    class ListPatients {
        @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
//    @Sql("/sql/test-data.sql")
        @Test
        public void findAllPatients_Success() throws Exception {
            mockMvc.perform(get(API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(7)));
        }

        @Test
        public void findAllPatients_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Display Patient By ID")
    @Nested
    class FindPatientById {
        @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
        @Test
        public void findPatientById_Success() throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Hsu Hsu")))
                    .andExpect(jsonPath("$.age", is(24)))
                    .andExpect(jsonPath("$.gender", is("FEMALE")))
                    .andExpect(jsonPath("$.phoneNumber", is("09222222222")));
        }

        @Test
        public void findPatientById_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
        @Test
        public void findPatientById_Not_Found() throws Exception {
            mockMvc.perform(get(API_ROOT + "/99999")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Patient Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @DisplayName("Create New Patient")
    @Nested
    class SaveNewPatient {
        @WithMockUser(roles = {"ADMIN", "PATIENT"})
        @Test
        public void saveNewPatient_Success() throws Exception {
            PatientResponseDTO newDto = PatientResponseDTO.builder()
                    .name("New Patient")
                    .age(24L)
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();

            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.name", is("New Patient")))
                    .andExpect(jsonPath("$.age", is(24)))
                    .andExpect(jsonPath("$.gender", is("FEMALE")))
                    .andExpect(jsonPath("$.phoneNumber", is("09222222222")));
        }

        @Test
        public void saveNewPatient_Unauthorized() throws Exception {
            PatientResponseDTO newDto = PatientResponseDTO.builder()
                    .name("New Patient")
                    .age(24L)
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();

            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"DOCTOR"})
        @Test
        public void saveNewPatient_Forbidden() throws Exception {
            PatientResponseDTO newDto = PatientResponseDTO.builder()
                    .name("New Patient")
                    .age(24L)
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();

            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    @DisplayName("Update Patient")
    @Nested
    class UpdatePatient {
        @WithMockUser(roles = {"ADMIN", "PATIENT"})
        @Test
        public void updatePatient_Success() throws Exception {
            PatientResponseDTO updatedDto = PatientResponseDTO.builder()
                    .name("Hsu Hsu")
                    .age(24L)
                    .gender(Gender.FEMALE)
                    .phoneNumber("09777777777")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();

            mockMvc.perform(put(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.phoneNumber", equalTo("09777777777")));
        }

        @Test
        public void updatePatient_Unauthorized() throws Exception {
            PatientResponseDTO updatedDto = PatientResponseDTO.builder()
                    .name("Hsu Hsu")
                    .age(24L)
                    .gender(Gender.FEMALE)
                    .phoneNumber("09777777777")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();

            mockMvc.perform(put(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"DOCTOR"})
        @Test
        public void updatePatient_Forbidden() throws Exception {
            PatientResponseDTO updatedDto = PatientResponseDTO.builder()
                    .name("Hsu Hsu")
                    .age(24L)
                    .gender(Gender.FEMALE)
                    .phoneNumber("09777777777")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();

            mockMvc.perform(put(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @WithMockUser(roles = {"ADMIN", "PATIENT"})
        @Test
        public void updatePatient_Not_Found() throws Exception {
            PatientResponseDTO updatedDto = PatientResponseDTO.builder()
                    .name("Hsu Hsu")
                    .age(24L)
                    .gender(Gender.FEMALE)
                    .phoneNumber("09777777777")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();

            mockMvc.perform(put(API_ROOT + "/99999")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Patient Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @DisplayName("Delete Patient")
    @Nested
    class DeletePatient {
        @WithMockUser(roles = {"ADMIN"})
        @Test
        public void deletePatientById_Success() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/7")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void deletePatientById_Unauthorized() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/7")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"DOCTOR", "PATIENT"})
        @Test
        public void deletePatientById_Forbidden() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/7")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @WithMockUser(roles = {"ADMIN"})
        @Test
        public void deletePatientById_Not_Found() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/99999")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Patient Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }
}
