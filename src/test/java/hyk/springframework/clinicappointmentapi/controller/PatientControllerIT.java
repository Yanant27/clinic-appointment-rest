package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientUpdateDTO;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import hyk.springframework.clinicappointmentapi.exception.ResourceAlreadyExistException;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import hyk.springframework.clinicappointmentapi.util.JsonStringUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class PatientControllerIT extends BaseIT {
    private final String API_ROOT = "/api/v1/patients";

    @Autowired
    private PatientRepository patientRepository;

    @DisplayName("Find All Patients")
    @Nested
    class ListPatients {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findAllPatients_Success(String role, String jwt) throws Exception {
            List<Patient> patients = patientRepository.findAll();
            mockMvc.perform(get(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(patients.size())));
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
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findPatientById_Success(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Hsu Hsu")))
                    .andExpect(jsonPath("$.dateOfBirth", is("1998-01-01")))
                    .andExpect(jsonPath("$.gender", is("FEMALE")))
                    .andExpect(jsonPath("$.phoneNumber", is("09222222222")))
                    .andExpect(jsonPath("$.address", is("No.30, Bo Ta Htaung Township, Yangon")))
                    .andExpect(jsonPath("$.appointments", notNullValue()))
                    .andExpect(jsonPath("$.createdBy", notNullValue()))
                    .andExpect(jsonPath("$.modifiedBy", notNullValue()));
        }

        @Test
        public void findPatientById_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findPatientById_Not_Found(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/99999")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof ResourceNotFoundException))
                    .andExpect(result -> assertEquals(
                            "Patient Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @DisplayName("Register New Patient")
    @Nested
    class RegisterPatient {
        private PatientRegistrationDTO newDto;
        @BeforeEach
        public void setup() {
            newDto = PatientRegistrationDTO.builder()
                    .username("newpatient")
                    .password("password")
                    .name("New Patient")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();
        }

        @Test
        public void registerPatient_Success() throws Exception {
            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andExpect(jsonPath("$.name", is("New Patient")))
                    .andExpect(jsonPath("$.dateOfBirth", is("1998-01-01")))
                    .andExpect(jsonPath("$.gender", is("FEMALE")))
                    .andExpect(jsonPath("$.phoneNumber", is("09222222222")))
                    .andExpect(jsonPath("$.address", is("No.30, Bo Ta Htaung Township, Yangon")))
                    .andExpect(jsonPath("$.createdBy", notNullValue()))
                    .andExpect(jsonPath("$.modifiedBy", notNullValue()));
        }

        @Test
        public void registerPatient_Fail_Username_Already_Exist() throws Exception {
            newDto = PatientRegistrationDTO.builder()
                    .username("hsuhsu")
                    .password("password")
                    .name("New Patient")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();
            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(result -> assertEquals(
                            "Username already exists !",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()))
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof ResourceAlreadyExistException));
        }

        @Test
        public void registerPatient_Fail_No_Username() throws Exception {
            newDto = PatientRegistrationDTO.builder()
                    .password("password")
                    .name("New Patient")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();
            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @Test
        public void registerPatient_Fail_No_Password() throws Exception {
            newDto = PatientRegistrationDTO.builder()
                    .username("newpatient")
                    .name("New Patient")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();
            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @Test
        public void registerPatient_Fail_No_Patient_Name() throws Exception {
            newDto = PatientRegistrationDTO.builder()
                    .username("newpatient")
                    .password("password")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();
            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @Test
        public void registerPatient_Fail_No_PhoneNumber() throws Exception {
            newDto = PatientRegistrationDTO.builder()
                    .username("newpatient")
                    .password("password")
                    .name("New Patient")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
//                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();
            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @Test
        public void registerPatient_Fail_Invalid_PhoneNumber() throws Exception {
            newDto = PatientRegistrationDTO.builder()
                    .username("newpatient")
                    .password("password")
                    .name("New Patient")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("0922222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();
            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }
    }

    @DisplayName("Update Patient")
    @Nested
    class UpdatePatient {
        PatientUpdateDTO updatedDto;

        @BeforeEach
        public void setup() {
            updatedDto = PatientUpdateDTO.builder()
                    .id(1L)
                    .name("Hsu Hsu")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09777777777") // update phonenumber
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminPatient")
        public void updatePatient_Success(String role, String jwt) throws Exception {
            mockMvc.perform(put(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andExpect(jsonPath("$.name", is("Hsu Hsu")))
                    .andExpect(jsonPath("$.dateOfBirth", is("1998-01-01")))
                    .andExpect(jsonPath("$.gender", is("FEMALE")))
                    .andExpect(jsonPath("$.phoneNumber", is("09777777777")))
                    .andExpect(jsonPath("$.address", is("No.30, Bo Ta Htaung Township, Yangon")))
                    .andExpect(jsonPath("$.appointments", notNullValue()))
                    .andExpect(jsonPath("$.createdBy", notNullValue()))
                    .andExpect(jsonPath("$.modifiedBy", notNullValue()));
        }

        @Test
        public void updatePatient_Unauthorized() throws Exception {
            mockMvc.perform(put(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getDoctor")
        public void updatePatient_Forbidden_Doctor_Role(String role, String jwt) throws Exception {
            mockMvc.perform(put(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getUnmatchedPatient")
        public void updatePatient_Forbidden_Unmatched_PatientID(String role, String jwt) throws Exception {
            mockMvc.perform(put(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void updatePatient_Fail_Patient_Not_Found(String role, String jwt) throws Exception {
            mockMvc.perform(put(API_ROOT + "/99999")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof ResourceNotFoundException))
                    .andExpect(result -> assertEquals(
                            "Patient Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminPatient")
        public void updatePatient_Fail_No_PatientID(String role, String jwt) throws Exception {
            updatedDto = PatientUpdateDTO.builder()
                    .name("Hsu Hsu")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();
            mockMvc.perform(put(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminPatient")
        public void updatePatient_Fail_No_Patient_Name(String role, String jwt) throws Exception {
            updatedDto = PatientUpdateDTO.builder()
                    .id(1L)
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();
            mockMvc.perform(put(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminPatient")
        public void updatePatient_Fail_No_PhoneNumber(String role, String jwt) throws Exception {
            updatedDto = PatientUpdateDTO.builder()
                    .id(1L)
                    .name("Hsu Hsu")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();
            mockMvc.perform(put(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminPatient")
        public void updatePatient_Fail_Invalid_PhoneNumber(String role, String jwt) throws Exception {
            updatedDto = PatientUpdateDTO.builder()
                    .id(1L)
                    .name("Hsu Hsu")
                    .dateOfBirth(LocalDate.of(1998, 1,1))
                    .gender(Gender.FEMALE)
                    .phoneNumber("09222222")
                    .address("No.30, Bo Ta Htaung Township, Yangon").build();
            mockMvc.perform(put(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }
    }

    @DisplayName("Delete Patient")
    @Nested
    class DeletePatient {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void deletePatientById_Success(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void deletePatientById_Unauthorized() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getDoctorPatient")
        public void deletePatientById_Forbidden(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void deletePatientById_Not_Found(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/99999")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof ResourceNotFoundException))
                    .andExpect(result -> assertEquals(
                            "Patient Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }
}
