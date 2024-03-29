package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleUpdateDTO;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
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

import java.time.DayOfWeek;
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
public class ScheduleControllerIT extends BaseIT {
    private final String API_ROOT = "/api/v1/schedules";

    @Autowired
    private ScheduleRepository scheduleRepository;

    @DisplayName("Find All Schedules")
    @Nested
    class ListSchedules {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findAllSchedules_Success(String role, String jwt) throws Exception {
            List<Schedule> schedules = scheduleRepository.findAll();
            mockMvc.perform(get(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(schedules.size())));
        }

        @Test
        public void findAllSchedules_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Find All Schedules by Doctor ID")
    @Nested
    class ListSchedulesByDoctorId {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findAllSchedulesByDoctorId_Success(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/doctors/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
        }

        @Test
        public void findAllSchedulesByDoctorId_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT + "/doctors/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findAllSchedulesByDoctorId_Fail_Not_Found(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/doctors/9999")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @DisplayName("Find Schedule By ID")
    @Nested
    class FindScheduleById {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findScheduleById_Success(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.dayOfWeek", is("SUNDAY")))
                    .andExpect(jsonPath("$.timeslot", is("09:00 ~ 10:00")))
                    .andExpect(jsonPath("$.doctorId", is(1)))
                    .andExpect(jsonPath("$.createdBy", notNullValue()))
                    .andExpect(jsonPath("$.modifiedBy", notNullValue()));
        }

        @Test
        public void findScheduleById_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAllUsers")
        public void findScheduleById_Fail_Not_Found(String role, String jwt) throws Exception {
            mockMvc.perform(get(API_ROOT + "/99999")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof ResourceNotFoundException))
                    .andExpect(result -> assertEquals(
                            "Schedule Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @DisplayName("Create New Schedule")
    @Nested
    class SaveNewSchedule {
        private ScheduleRegistrationDTO newDto;

        @BeforeEach
        public void setup() {
            newDto = ScheduleRegistrationDTO.builder()
                    .dayOfWeek(DayOfWeek.MONDAY.name())
                    .timeslot("10:00 ~ 11:00")
                    .doctorId(1L)
                    .build();
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void saveNewSchedule_Success(String role, String jwt) throws Exception {
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.dayOfWeek", is("MONDAY")))
                    .andExpect(jsonPath("$.timeslot", is("10:00 ~ 11:00")))
                    .andExpect(jsonPath("$.doctorId", is(1)))
                    .andExpect(jsonPath("$.createdBy", notNullValue()))
                    .andExpect(jsonPath("$.modifiedBy", notNullValue()));
        }

        @Test
        public void saveNewSchedule_Unauthorized() throws Exception {
            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getPatient")
        public void saveNewSchedule_Forbidden_Patient_Role(String role, String jwt) throws Exception {
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getUnmatchedDoctor")
        public void saveNewSchedule_Forbidden_Unmatched_DoctorID(String role, String jwt) throws Exception {
            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void saveNewSchedule_Fail_Doctor_Not_Found(String role, String jwt) throws Exception {
            newDto = ScheduleResponseDTO.builder()
                    .dayOfWeek(DayOfWeek.MONDAY.name())
                    .timeslot("10:00 ~ 11:00")
                    .doctorId(99999L)
                    .build();

            mockMvc.perform(post(API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(newDto))
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
        public void saveNewSchedule_Fail_No_DayOfWeek(String role, String jwt) throws Exception {
            newDto = ScheduleRegistrationDTO.builder()
                    .timeslot("10:00 ~ 11:00")
                    .doctorId(1L)
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
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void saveNewSchedule_Fail_No_Timeslot(String role, String jwt) throws Exception {
            newDto = ScheduleRegistrationDTO.builder()
                    .dayOfWeek(DayOfWeek.MONDAY.name())
//                    .timeslot("10:00 ~ 11:00")
                    .doctorId(1L)
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
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void saveNewSchedule_Fail_No_DoctorID(String role, String jwt) throws Exception {
            newDto = ScheduleRegistrationDTO.builder()
                    .dayOfWeek(DayOfWeek.MONDAY.name())
                    .timeslot("10:00 ~ 11:00")
//                    .doctorId(1L)
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

    @DisplayName("Update Schedule")
    @Nested
    class UpdateSchedule {
        private ScheduleUpdateDTO updatedDto;

        @BeforeEach
        public void setup() {
            updatedDto = ScheduleResponseDTO.builder()
                    .id(1L)
                    .dayOfWeek(DayOfWeek.MONDAY.name())
                    .timeslot("15:00 ~ 16:00") // change timeslot
                    .doctorId(1L)
                    .build();
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void updateSchedule_Success(String role, String jwt) throws Exception {
            mockMvc.perform(put(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.dayOfWeek", is("MONDAY")))
                    .andExpect(jsonPath("$.timeslot", is("15:00 ~ 16:00")))
                    .andExpect(jsonPath("$.doctorId", is(1)))
                    .andExpect(jsonPath("$.createdBy", notNullValue()))
                    .andExpect(jsonPath("$.modifiedBy", notNullValue()));
        }

        @Test
        public void updateSchedule_Unauthorized() throws Exception {
            mockMvc.perform(put(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getPatient")
        public void updateSchedule_Forbidden_Patient_Role(String role, String jwt) throws Exception {
            mockMvc.perform(put(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getUnmatchedDoctor")
        public void updateSchedule_Forbidden_Unmatched_DoctorID(String role, String jwt) throws Exception {
            mockMvc.perform(put(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void updateSchedule_Fail_Schedule_Not_Found(String role, String jwt) throws Exception {
            mockMvc.perform(put(API_ROOT + "/99999")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof ResourceNotFoundException))
                    .andExpect(result -> assertEquals(
                            "Schedule Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void updateSchedule_Fail_Doctor_Not_Found(String role, String jwt) throws Exception {
            updatedDto = ScheduleUpdateDTO.builder()
                    .id(1L)
                    .dayOfWeek(DayOfWeek.MONDAY.name())
                    .timeslot("15:00 ~ 16:00") // change timeslot
                    .doctorId(99999L)
                    .build();

            mockMvc.perform(put(API_ROOT + "/1")
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
        public void updateSchedule_Fail_No_ScheduleID(String role, String jwt) throws Exception {
            updatedDto = ScheduleUpdateDTO.builder()
                    .dayOfWeek(DayOfWeek.MONDAY.name())
                    .timeslot("10:00 ~ 11:00")
                    .doctorId(1L)
                    .build();

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
        public void updateSchedule_Fail_No_DayOfWeek(String role, String jwt) throws Exception {
            updatedDto = ScheduleUpdateDTO.builder()
                    .timeslot("10:00 ~ 11:00")
                    .doctorId(1L)
                    .build();

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
        public void updateSchedule_Fail_No_Timeslot(String role, String jwt) throws Exception {
            updatedDto = ScheduleUpdateDTO.builder()
                    .dayOfWeek(DayOfWeek.MONDAY.name())
                    .doctorId(1L)
                    .build();

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
        public void updateSchedule_Fail_No_DoctorID(String role, String jwt) throws Exception {
            updatedDto = ScheduleUpdateDTO.builder()
                    .dayOfWeek(DayOfWeek.MONDAY.name())
                    .timeslot("10:00 ~ 11:00")
                    .build();

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

    @DisplayName("Delete Schedule")
    @Nested
    class DeleteSchedule {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void deleteScheduleById_Success(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void deleteScheduleById_Unauthorized() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getPatient")
        public void deleteScheduleById_Forbidden_Patient_Role(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getUnmatchedDoctor")
        public void deleteScheduleById_Forbidden_Unmatched_DoctorID(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/1")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdminDoctor")
        public void deleteScheduleById_Fail_Not_Found(String role, String jwt) throws Exception {
            mockMvc.perform(delete(API_ROOT + "/99999")
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof ResourceNotFoundException))
                    .andExpect(result -> assertEquals(
                            "Schedule Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }
}
