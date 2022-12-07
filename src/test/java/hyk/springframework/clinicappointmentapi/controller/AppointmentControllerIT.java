package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentUpdateStatusDTO;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
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
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AppointmentControllerIT extends BaseIT {
    private final String API_ROOT = "/api/v1/appointments";

    @Autowired
    private AppointmentRepository appointmentRepository;

    @DisplayName("Find All Appointments")
    @Nested
    class ListAppointments {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void findAllAppointments_Success(String role, String jwt) throws Exception {
            List<Appointment> appointments = appointmentRepository.findAll();
            mockMvc.perform(get(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(appointments.size())));
        }

        @Test
        public void findAllAppointments_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Find All Appointments by Doctor ID")
    @Nested
    class ListAppointmentsByDoctorId {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void findAllAppointmentsByDoctorId_Success(String role, String jwt) throws Exception {
            List<Appointment> appointments = appointmentRepository.findAllByDoctorId(1L);
            mockMvc.perform(get(API_ROOT + "/doctors/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(appointments.size())));
        }

        @Test
        public void findAllAppointmentsByDoctorId_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getPatient")
        public void findAllAppointmentsByDoctorId_Forbidden(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/doctors/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getUnmatchedDoctor")
        public void findAllAppointmentsByDoctorId_Forbidden_Unmatched_DoctorID(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/doctors/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    @DisplayName("Find All Appointments By Patient ID")
    @Nested
    class ListAppointmentsByPatientId {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminPatient")
        public void findAllAppointmentsByPatientId_Success(String role, String jwt) throws Exception {
            List<Appointment> appointments = appointmentRepository.findAllByPatientId(1L);
            mockMvc.perform(get(API_ROOT + "/patients/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(appointments.size())));
        }

        @Test
        public void findAllAppointmentsByPatientId_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getDoctor")
        public void findAllAppointmentsByPatientId_Forbidden(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/patients/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getUnmatchedPatient")
        public void findAllAppointmentsByPatientId_Forbidden_Unmatched_PatientID(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/patients/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    @DisplayName("Display Appointment By ID")
    @Nested
    class FindAppointmentById {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findAppointmentById_Success(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.doctorId", is(1)))
                    .andExpect(jsonPath("$.doctorName", is("Dr. Lin Htet")))
                    .andExpect(jsonPath("$.specialization", is("Internal medicine")))
                    .andExpect(jsonPath("$.patientId", is(1)))
                    .andExpect(jsonPath("$.patientName", is("Hsu Hsu")))
                    .andExpect(jsonPath("$.patientPhoneNumber", is("09222222222")))
                    .andExpect(jsonPath("$.appointmentDate", is(LocalDate.now().plusDays(3).toString())))
                    .andExpect(jsonPath("$.appointmentStatus", is("PENDING")))
                    .andExpect(jsonPath("$.timeslot", is("09:00 ~ 10:00")))
                    .andExpect(jsonPath("$.createdBy", notNullValue()))
                    .andExpect(jsonPath("$.modifiedBy", notNullValue()));
        }

        @Test
        public void findAppointmentById_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getUnmatchedDoctor")
        public void findAppointmentById_Forbidden_Unmatched_Doctor(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getUnmatchedPatient")
        public void findAppointmentById_Forbidden_Unmatched_Patient(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findAppointmentById_Not_Found(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/99999")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Appointment Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @DisplayName("Register New Appointment")
    @Nested
    class RegisterAppointment {
        private AppointmentRegistrationDTO newDto;

        @BeforeEach
        public void setup() {
            newDto = AppointmentRegistrationDTO.builder()
                    .doctorId(1L)
                    .patientId(1L)
                    .timeslot("09:00 ~ 10:00")
                    .appointmentDate(LocalDate.now().plusDays(3))
                    .build();
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminPatient")
        public void registerSchedule_Success(String role, String jwt) throws Exception {
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.doctorId", is(1)))
                    .andExpect(jsonPath("$.doctorName", is("Dr. Lin Htet")))
                    .andExpect(jsonPath("$.specialization", is("Internal medicine")))
                    .andExpect(jsonPath("$.patientId", is(1)))
                    .andExpect(jsonPath("$.patientName", is("Hsu Hsu")))
                    .andExpect(jsonPath("$.patientPhoneNumber", is("09222222222")))
                    .andExpect(jsonPath("$.appointmentDate", is(LocalDate.now().plusDays(3).toString())))
                    .andExpect(jsonPath("$.appointmentStatus", is("PENDING")))
                    .andExpect(jsonPath("$.timeslot", is("09:00 ~ 10:00")))
                    .andExpect(jsonPath("$.createdBy", notNullValue()))
                    .andExpect(jsonPath("$.modifiedBy", notNullValue()));
        }

        @Test
        public void registerSchedule_Unauthorized() throws Exception {
            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getDoctor")
        public void registerSchedule_Forbidden(String role, String jwt) throws Exception {
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminPatient")
        public void registerSchedule_Fail_No_DoctorID(String role, String jwt) throws Exception {
            newDto = AppointmentRegistrationDTO.builder()
                    .patientId(1L)
                    .timeslot("09:00 ~ 10:00")
                    .appointmentDate(LocalDate.now().plusDays(3))
                    .build();
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
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminPatient")
        public void registerSchedule_Fail_No_PatientID(String role, String jwt) throws Exception {
            newDto = AppointmentRegistrationDTO.builder()
                    .doctorId(1L)
                    .timeslot("09:00 ~ 10:00")
                    .appointmentDate(LocalDate.now().plusDays(3))
                    .build();
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
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminPatient")
        public void registerSchedule_Fail_No_Timeslot(String role, String jwt) throws Exception {
            newDto = AppointmentRegistrationDTO.builder()
                    .doctorId(1L)
                    .patientId(1L)
                    .appointmentDate(LocalDate.now().plusDays(3))
                    .build();
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
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminPatient")
        public void registerSchedule_Fail_No_Appointment_Date(String role, String jwt) throws Exception {
            newDto = AppointmentRegistrationDTO.builder()
                    .doctorId(1L)
                    .patientId(1L)
                    .timeslot("09:00 ~ 10:00")
                    .build();
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

    @DisplayName("Update Appointment")
    @Nested
    class UpdateAppointment {
        private AppointmentUpdateStatusDTO updatedDto;

        @BeforeEach
        public void setup() {
            updatedDto = AppointmentUpdateStatusDTO.builder()
                    .id(1L)
                    .appointmentStatus(AppointmentStatus.APPROVED)
                    .build();
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void updateSchedule_Success(String role, String jwt) throws Exception {
            mockMvc.perform(patch(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.doctorId", is(1)))
                    .andExpect(jsonPath("$.doctorName", is("Dr. Lin Htet")))
                    .andExpect(jsonPath("$.specialization", is("Internal medicine")))
                    .andExpect(jsonPath("$.patientId", is(1)))
                    .andExpect(jsonPath("$.patientName", is("Hsu Hsu")))
                    .andExpect(jsonPath("$.patientPhoneNumber", is("09222222222")))
                    .andExpect(jsonPath("$.appointmentDate", is(LocalDate.now().plusDays(3).toString())))
                    .andExpect(jsonPath("$.appointmentStatus", is("APPROVED")))
                    .andExpect(jsonPath("$.timeslot", is("09:00 ~ 10:00")))
                    .andExpect(jsonPath("$.createdBy", notNullValue()))
                    .andExpect(jsonPath("$.modifiedBy", notNullValue()));
        }

        @Test
        public void updateSchedule_Unauthorized() throws Exception {
            mockMvc.perform(patch(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getPatient")
        public void updateSchedule_Forbidden(String role, String jwt) throws Exception {
            mockMvc.perform(patch(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getUnmatchedDoctor")
        public void updateSchedule_Forbidden_Unmatched_Doctor(String role, String jwt) throws Exception {
            mockMvc.perform(patch(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void updateSchedule_Fail_No_AppointmentID(String role, String jwt) throws Exception {
            updatedDto = AppointmentUpdateStatusDTO.builder()
                    .appointmentStatus(AppointmentStatus.APPROVED)
                    .build();
            mockMvc.perform(patch(API_ROOT + "/1")
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
        public void updateSchedule_Fail_No_AppointmentStatus(String role, String jwt) throws Exception {
            updatedDto = AppointmentUpdateStatusDTO.builder()
                    .id(1L)
                    .build();
            mockMvc.perform(patch(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof MethodArgumentNotValidException));
        }
    }

    @DisplayName("Delete Appointment By ID")
    @Nested
    class DeleteAppointmentById {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void deleteAppointmentById_Success(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void deleteAppointmentById_Unauthorized() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getDoctorPatient")
        public void deleteAppointmentById_Forbidden(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void deleteAppointmentById_Not_Found(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/99999")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Appointment Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }
}
