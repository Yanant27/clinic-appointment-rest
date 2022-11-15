package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorResponseDTO;
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
public class DoctorControllerIT {
    private final String API_ROOT = "/api/v1/doctors";

    private final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;

    @Autowired
    private MockMvc mockMvc;


    @DisplayName("Find All Doctors")
    @Nested
    class ListDoctors {
        @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
        @Test
        public void findAllDoctors_Success() throws Exception {
            mockMvc.perform(get(API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(7)));
        }

        @Test
        public void findAllDoctors_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Display Doctor By ID")
    @Nested
    class FindDoctorsById {
        @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
        @Test
        public void findDoctorById_Success() throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Dr. Lin Htet")))
                    .andExpect(jsonPath("$.age", is(32)))
                    .andExpect(jsonPath("$.gender", is("MALE")))
                    .andExpect(jsonPath("$.scheduleResponseDTOS", notNullValue()))
                    .andExpect(jsonPath("$.appointmentResponseDTOS", notNullValue()));
        }

        @Test
        public void findDoctorById_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
        @Test
        public void findDoctorById_Not_Found() throws Exception {
            mockMvc.perform(get(API_ROOT + "/99999")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Doctor Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @DisplayName("Create New Doctor")
    @Nested
    class SaveNewDoctor {
        @WithMockUser(roles = {"ADMIN", "DOCTOR"})
        @Test
        public void saveNewDoctor_Success() throws Exception {
            DoctorResponseDTO newDto = DoctorResponseDTO.builder()
                    .name("New Doctor")
                    .age(32L)
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();

            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.name", is("New Doctor")))
                    .andExpect(jsonPath("$.age", is(32)))
                    .andExpect(jsonPath("$.gender", is("MALE")))
                    .andExpect(jsonPath("$.phoneNumber", is("09111111111")))
                    .andExpect(jsonPath("$.qualifications", is("MBBS")))
                    .andExpect(jsonPath("$.specialization", is("Internal medicine")));
        }

        @Test
        public void saveNewDoctor_Unauthorized() throws Exception {
            DoctorResponseDTO newDto = DoctorResponseDTO.builder()
                    .name("New Doctor")
                    .age(32L)
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();

            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"PATIENT"})
        @Test
        public void saveNewDoctor_Forbidden() throws Exception {
            DoctorResponseDTO newDto = DoctorResponseDTO.builder()
                    .name("New Doctor")
                    .age(32L)
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();

            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    @DisplayName("Update Doctor")
    @Nested
    class UpdateDoctor {
        @WithMockUser(roles = {"ADMIN", "DOCTOR"})
        @Test
        public void updateDoctor_Success() throws Exception {
            DoctorResponseDTO updatedDto = DoctorResponseDTO.builder()
                    .name("Dr. Lin Htet")
                    .age(32L)
                    .gender(Gender.MALE)
                    .phoneNumber("09999999999")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine")
                    .build();

            mockMvc.perform(put(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Dr. Lin Htet")))
                    .andExpect(jsonPath("$.age", is(32)))
                    .andExpect(jsonPath("$.gender", is("MALE")))
                    .andExpect(jsonPath("$.phoneNumber", is("09999999999")))
                    .andExpect(jsonPath("$.qualifications", is("MBBS")))
                    .andExpect(jsonPath("$.specialization", is("Internal medicine")));
        }

        @Test
        public void updateDoctor_Unauthorized() throws Exception {
            DoctorResponseDTO updatedDto = DoctorResponseDTO.builder()
                    .name("Dr. Lin Htet")
                    .age(32L)
                    .gender(Gender.MALE)
                    .phoneNumber("09999999999")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine")
                    .build();

            mockMvc.perform(put(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }


        @WithMockUser(roles = {"PATIENT"})
        @Test
        public void updateDoctor_Forbidden() throws Exception {
            DoctorResponseDTO updatedDto = DoctorResponseDTO.builder()
                    .name("Dr. Lin Htet")
                    .age(32L)
                    .gender(Gender.MALE)
                    .phoneNumber("09999999999")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine")
                    .build();

            mockMvc.perform(put(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @WithMockUser(roles = {"ADMIN", "PATIENT"})
        @Test
        public void updatePatient_Not_Found() throws Exception {
            DoctorResponseDTO updatedDto = DoctorResponseDTO.builder()
                    .name("Dr. Lin Htet")
                    .age(32L)
                    .gender(Gender.MALE)
                    .phoneNumber("09999999999")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine")
                    .build();

            mockMvc.perform(put(API_ROOT + "/99999")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Doctor Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @DisplayName("Delete Doctor")
    @Nested
    class DeleteDoctor {
        @WithMockUser(roles = {"ADMIN"})
        @Test
        public void deleteDoctorById_Success() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/7")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void deleteDoctorById_Unauthorized() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/7")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"DOCTOR"})
        @Test
        public void deleteDoctorById_Forbidden() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/7")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @WithMockUser(roles = {"ADMIN"})
        @Test
        public void deleteDoctorById_Not_Found() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/99999")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Doctor Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @DisplayName("Find All Doctors by Specialization")
    @Nested
    class ListDoctorsBySpecialization {
        @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
        @Test
        public void findAllDoctorsBySpecialization_Success() throws Exception {
            mockMvc.perform(get(API_ROOT + "/specializations/cardiology")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
        }

        @Test
        public void findAllDoctorsBySpecialization_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT + "/specializations/Cardiology")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }
}
