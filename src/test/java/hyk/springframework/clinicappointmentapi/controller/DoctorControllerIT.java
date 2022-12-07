package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorUpdateDTO;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import hyk.springframework.clinicappointmentapi.exception.ResourceAlreadyExistException;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
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
public class DoctorControllerIT extends BaseIT {
    private final String API_ROOT = "/api/v1/doctors";

    @Autowired
    private DoctorRepository doctorRepository;

    @DisplayName("Find All Doctors")
    @Nested
    class ListDoctors {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findAllDoctors_Success(String role, String jwt) throws Exception {
            List<Doctor> doctors = doctorRepository.findAll();
            mockMvc.perform(get(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(doctors.size())));
        }

        @Test
        public void findAllDoctors_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Find All Doctors By Specialization")
    @Nested
    class ListDoctorsBySpecialization {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findAllDoctorsBySpecialization_Success(String role, String jwt) throws Exception {
            long count = doctorRepository.findAll().stream()
                    .filter(doctor -> doctor.getSpecialization().equalsIgnoreCase("Internal medicine"))
                    .count();
            mockMvc.perform(get(API_ROOT + "/specializations/Internal medicine")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize((int) count)));
        }

        @Test
        public void findAllDoctorsBySpecialization_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Display Doctor By ID")
    @Nested
    class FindDoctorsById {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findDoctorById_Success(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Dr. Lin Htet")))
                    .andExpect(jsonPath("$.gender", is("MALE")))
                    .andExpect(jsonPath("$.address", is("No.1, 1st street, Than Lyin")))
                    .andExpect(jsonPath("$.qualifications", is("MBBS")))
                    .andExpect(jsonPath("$.specialization", is("Internal medicine")))
                    .andExpect(jsonPath("$.scheduleResponseDTOS", notNullValue()))
                    .andExpect(jsonPath("$.appointmentResponseDTOS", notNullValue()))
                    .andExpect(jsonPath("$.createdBy", notNullValue()))
                    .andExpect(jsonPath("$.modifiedBy", notNullValue()));
        }

        @Test
        public void findDoctorById_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findDoctorById_Not_Found(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/99999")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof ResourceNotFoundException))
                    .andExpect(result -> assertEquals(
                            "Doctor Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @DisplayName("Create New Doctor")
    @Nested
    class SaveNewDoctor {
        private DoctorRegistrationDTO newDto;

        @BeforeEach
        public void setup() {
            newDto = DoctorRegistrationDTO.builder()
                    .username("newdoctor")
                    .password("password")
                    .name("New Doctor")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();
        }
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void saveNewDoctor_Success(String role, String jwt) throws Exception {
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andExpect(jsonPath("$.name", is("New Doctor")))
                    .andExpect(jsonPath("$.gender", is("MALE")))
                    .andExpect(jsonPath("$.phoneNumber", is("09111111111")))
                    .andExpect(jsonPath("$.qualifications", is("MBBS")))
                    .andExpect(jsonPath("$.specialization", is("Internal medicine")))
                    .andExpect(jsonPath("$.createdBy", notNullValue()))
                    .andExpect(jsonPath("$.modifiedBy", notNullValue()));
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void saveNewDoctor_Username_Already_Exist(String role, String jwt) throws Exception {
            newDto = DoctorRegistrationDTO.builder()
                    .username("linhtet")
                    .password("password")
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
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
        public void saveNewDoctor_Unauthorized() throws Exception {
            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getPatient")
        public void saveNewDoctor_Forbidden_Patient_Role(String role, String jwt) throws Exception {
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void saveNewDoctor_Fail_No_Username(String role, String jwt) throws Exception {
            newDto = DoctorRegistrationDTO.builder()
                    .password("password")
                    .name("New Doctor")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void saveNewDoctor_Fail_No_Password(String role, String jwt) throws Exception {
            newDto = DoctorRegistrationDTO.builder()
                    .username("newdoctor")
                    .name("New Doctor")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void saveNewDoctor_Fail_No_Doctor_Name(String role, String jwt) throws Exception {
            newDto = DoctorRegistrationDTO.builder()
                    .username("newdoctor")
                    .password("password")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void saveNewDoctor_Fail_No_Qualifications(String role, String jwt) throws Exception {
            newDto = DoctorRegistrationDTO.builder()
                    .username("newdoctor")
                    .password("password")
                    .name("New Doctor")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .specialization("Internal medicine").build();
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void saveNewDoctor_Fail_No_Specialization(String role, String jwt) throws Exception {
            newDto = DoctorRegistrationDTO.builder()
                    .username("newdoctor")
                    .password("password")
                    .name("New Doctor")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09111111111")
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS").build();
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }
    }

    @DisplayName("Update Doctor")
    @Nested
    class UpdateDoctor {
        private DoctorUpdateDTO updatedDto;

        @BeforeEach
        public void setup() {
            updatedDto = DoctorUpdateDTO.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09999999999") // update phone number
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void updateDoctor_Success(String role, String jwt) throws Exception {
            mockMvc.perform(put(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Dr. Lin Htet")))
                    .andExpect(jsonPath("$.gender", is("MALE")))
                    .andExpect(jsonPath("$.phoneNumber", is("09999999999")))
                    .andExpect(jsonPath("$.qualifications", is("MBBS")))
                    .andExpect(jsonPath("$.specialization", is("Internal medicine")))
                    .andExpect(jsonPath("$.scheduleResponseDTOS", notNullValue()))
                    .andExpect(jsonPath("$.appointmentResponseDTOS", notNullValue()))
                    .andExpect(jsonPath("$.createdBy", notNullValue()))
                    .andExpect(jsonPath("$.modifiedBy", notNullValue()));
        }

        @Test
        public void updateDoctor_Unauthorized() throws Exception {
            mockMvc.perform(put(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getUnmatchedDoctor")
        public void updateDoctor_Forbidden_Unmatched_DoctorID(String role, String jwt) throws Exception {
            mockMvc.perform(put(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void updatePatient_Fail_Doctor_Not_Found(String role, String jwt) throws Exception {
            mockMvc.perform(put(API_ROOT + "/99999")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof ResourceNotFoundException))
                    .andExpect(result -> assertEquals(
                            "Doctor Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void updateDoctor_Fail_No_DoctorID(String role, String jwt) throws Exception {
            updatedDto = DoctorUpdateDTO.builder()
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09999999999") // update phone number
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();
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
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void updateDoctor_Fail_No_Doctor_Name(String role, String jwt) throws Exception {
            updatedDto = DoctorUpdateDTO.builder()
                    .id(1L)
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09999999999") // update phone number
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();
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
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void updateDoctor_Fail_No_PhoneNumber(String role, String jwt) throws Exception {
            updatedDto = DoctorUpdateDTO.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();
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
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void updateDoctor_Fail_Invalid_PhoneNumber(String role, String jwt) throws Exception {
            updatedDto = DoctorUpdateDTO.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("099999") // update phone number
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS")
                    .specialization("Internal medicine").build();
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
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void updateDoctor_Fail_No_Qualifications(String role, String jwt) throws Exception {
            updatedDto = DoctorUpdateDTO.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09999999999") // update phone number
                    .address("No.1, 1st street, Than Lyin")
                    .specialization("Internal medicine").build();
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
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void updateDoctor_Fail_No_Specialization(String role, String jwt) throws Exception {
            updatedDto = DoctorUpdateDTO.builder()
                    .id(1L)
                    .name("Dr. Lin Htet")
                    .dateOfBirth(LocalDate.of(1990,1,1))
                    .gender(Gender.MALE)
                    .phoneNumber("09999999999") // update phone number
                    .address("No.1, 1st street, Than Lyin")
                    .qualifications("MBBS").build();
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

    @DisplayName("Delete Doctor")
    @Nested
    class DeleteDoctor {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void deleteDoctorById_Success(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void deleteDoctorById_Unauthorized() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getDoctorPatient")
        public void deleteDoctorById_Forbidden(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void deleteDoctorById_Not_Found(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/99999")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Doctor Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }
}
