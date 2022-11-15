package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
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

import java.time.DayOfWeek;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class ScheduleControllerIT {
    private final String API_ROOT = "/api/v1/schedules";

    private final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;

    @Autowired
    private MockMvc mockMvc;


    @DisplayName("Find All Schedules")
    @Nested
    class ListSchedules {
        @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
        @Test
        public void findAllSchedules_Success() throws Exception {
            mockMvc.perform(get(API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(11)));
        }

        @Test
        public void findAllSchedules_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Display Schedule By ID")
    @Nested
    class FindScheduleById {
        @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
        @Test
        public void findScheduleById_Success() throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.dayOfWeek", is("SUNDAY")))
                    .andExpect(jsonPath("$.timeslot", is("09:00 ~ 10:00")))
                    .andExpect(jsonPath("$.scheduleStatus", is("OCCUPIED")))
                    .andExpect(jsonPath("$.doctorName", is("Dr. Lin Htet")))
                    .andExpect(jsonPath("$.specialization", is("Internal medicine")));
        }

        @Test
        public void findScheduleById_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT + "/1")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
        @Test
        public void findScheduleById_Not_Found() throws Exception {
            mockMvc.perform(get(API_ROOT + "/99999")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Schedule Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @DisplayName("Create New Schedule")
    @Nested
    class SaveNewSchedule {
        @WithMockUser(roles = {"ADMIN", "DOCTOR"})
        @Test
        public void saveNewSchedule_Success() throws Exception {
            ScheduleResponseDTO newDto = ScheduleResponseDTO.builder()
                    .dayOfWeek(DayOfWeek.MONDAY.name())
                    .timeslot("10:00 ~ 11:00")
                    .doctorId(1L)
                    .build();

            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.dayOfWeek", is("MONDAY")))
                    .andExpect(jsonPath("$.timeslot", is("10:00 ~ 11:00")))
                    .andExpect(jsonPath("$.scheduleStatus", is("AVAILABLE")));
        }

        @Test
        public void saveNewSchedule_Unauthorized() throws Exception {
            ScheduleResponseDTO newDto = ScheduleResponseDTO.builder()
                    .dayOfWeek(DayOfWeek.MONDAY.name())
                    .timeslot("10:00 ~ 11:00")
                    .doctorId(1L)
                    .build();

            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"PATIENT"})
        @Test
        public void saveNewSchedule_Forbidden() throws Exception {
            ScheduleResponseDTO newDto = ScheduleResponseDTO.builder()
                    .dayOfWeek(DayOfWeek.MONDAY.name())
                    .timeslot("10:00 ~ 11:00")
                    .doctorId(1L)
                    .build();

            mockMvc.perform(post(API_ROOT)
                            .content(JsonStringUtil.asJsonString(newDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    @DisplayName("Update Schedule")
    @Nested
    class UpdateSchedule {
        @WithMockUser(roles = {"ADMIN", "DOCTOR"})
        @Test
        public void updateSchedule_Success() throws Exception {
            ScheduleResponseDTO updatedDto = ScheduleResponseDTO.builder()
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("15:00 ~ 16:00")
                    .doctorId(1L)
                    .build();

            mockMvc.perform(put(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.dayOfWeek", is("SUNDAY")))
                    .andExpect(jsonPath("$.timeslot", is("15:00 ~ 16:00")));
        }

        @Test
        public void updateSchedule_Unauthorized() throws Exception {
            ScheduleResponseDTO updatedDto = ScheduleResponseDTO.builder()
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("15:00 ~ 16:00")
                    .build();

            mockMvc.perform(put(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"PATIENT"})
        @Test
        public void updateSchedule_Forbidden() throws Exception {
            ScheduleResponseDTO updatedDto = ScheduleResponseDTO.builder()
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("15:00 ~ 16:00")
                    .build();

            mockMvc.perform(put(API_ROOT + "/1")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @WithMockUser(roles = {"ADMIN", "DOCTOR"})
        @Test
        public void updateSchedule_Not_Found() throws Exception {
            ScheduleResponseDTO updatedDto = ScheduleResponseDTO.builder()
                    .dayOfWeek(DayOfWeek.SUNDAY.name())
                    .timeslot("15:00 ~ 16:00")
                    .build();

            mockMvc.perform(put(API_ROOT + "/99999")
                            .content(JsonStringUtil.asJsonString(updatedDto))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Schedule Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @DisplayName("Delete Schedule")
    @Nested
    class DeleteSchedule {
        @WithMockUser(roles = {"ADMIN", "DOCTOR"})
        @Test
        public void deleteScheduleById_Success() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/11")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void deleteScheduleById_Unauthorized() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/11")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"PATIENT"})
        @Test
        public void deleteScheduleById_Forbidden() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/11")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @WithMockUser(roles = {"ADMIN", "DOCTOR"})
        @Test
        public void deleteScheduleById_Not_Found() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/99999")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Schedule Not Found. ID: 99999",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }

    @DisplayName("Find All Schedules by Doctor ID")
    @Nested
    class ListSchedulesByDoctorId {
        @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
        @Test
        public void findAllDoctorsBySpecialization_Success() throws Exception {
            mockMvc.perform(get(API_ROOT + "/doctors/2")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        }

        @Test
        public void findAllDoctorsBySpecialization_Unauthorized() throws Exception {
            mockMvc.perform(get(API_ROOT + "/doctors/2")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"ADMIN", "DOCTOR", "PATIENT"})
        @Test
        public void findAllDoctorsBySpecialization_Not_Found() throws Exception {
            mockMvc.perform(get(API_ROOT + "/doctors/22")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @DisplayName("Logically Delete Schedule by Doctor Id")
    @Nested
    class LogicalDeleteByDoctorId {
        @WithMockUser(roles = {"ADMIN", "DOCTOR"})
        @Test
        public void logicalDeleteScheduleByDoctorId_Success() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/9/doctors/7")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void logicalDeleteScheduleByDoctorId_Unauthorized() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/9/doctors/7")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @WithMockUser(roles = {"PATIENT"})
        @Test
        public void logicalDeleteScheduleByDoctorId_Forbidden() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/9/doctors/7")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }

        @WithMockUser(roles = {"ADMIN", "DOCTOR"})
        @Test
        public void logicalDeleteScheduleByDoctorId_Not_Found() throws Exception {
            mockMvc.perform(delete(API_ROOT + "/9/doctors/77")
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(
                            "Schedule Not Found. Schedule ID: 9, Doctor ID: 77",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()));
        }
    }
}
