package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.dto.security.RoleResponse;
import hyk.springframework.clinicappointmentapi.dto.security.UserAuthenticationDTO;
import hyk.springframework.clinicappointmentapi.dto.security.UserRegistrationDTO;
import hyk.springframework.clinicappointmentapi.util.JsonStringUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class LoginControllerIT extends BaseIT {
    private final String REGISTER_API_ROOT = "/registerAdmin";

    private final String AUTHENTICATE_API_ROOT = "/authenticate";

    @DisplayName("Register Admin")
    @Nested
    class RegisterAdmin {
        private UserRegistrationDTO userRegistrationDTO;

        @BeforeEach
        public void setup() {
            userRegistrationDTO = UserRegistrationDTO.builder()
                    .username("admin2")
                    .password("admin2")
                    .roles(Set.of("ADMIN"))
                    .build();
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAdmin")
        public void registerAdmin_Success(String role, String jwt) throws Exception {
            mockMvc.perform(post(REGISTER_API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(userRegistrationDTO))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andExpect(jsonPath("$.username", is("admin2")))
                    .andExpect(jsonPath("$.roles", notNullValue(RoleResponse.class)))
                    .andExpect(jsonPath("$.accountNonLocked", is(true)))
                    .andExpect(jsonPath("$.enabled", is(true)));
        }

        @Test
        public void registerAdmin_Unauthorized() throws Exception {
            mockMvc.perform(post(REGISTER_API_ROOT)
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getDoctorPatient")
        public void registerAdmin_Forbidden(String role, String jwt) throws Exception {
            mockMvc.perform(post(REGISTER_API_ROOT)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .content(JsonStringUtil.asJsonString(userRegistrationDTO))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    @DisplayName("Authenticate User")
    @Nested
    class AuthenticateUser {
        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getAuthenticatedUsers")
        public void authenticate_Success(String name, UserAuthenticationDTO request) throws Exception {
            mockMvc.perform(get(AUTHENTICATE_API_ROOT)
                            .content(JsonStringUtil.asJsonString(request))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getUserWithInvalidUsername")
        public void authenticate_Fail_Invalid_Username(String name, UserAuthenticationDTO request) throws Exception {
            mockMvc.perform(get(AUTHENTICATE_API_ROOT)
                            .content(JsonStringUtil.asJsonString(request))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(result -> assertEquals(
                            "No user registered with this username !",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()))
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof BadCredentialsException));
        }

        @ParameterizedTest(name = "Test-{index} with {0}")
        @MethodSource("hyk.springframework.clinicappointmentapi.controller.BaseIT#getUserWithIncorrectPassword")
        public void authenticate_Fail_Incorrect_Password(String name, UserAuthenticationDTO request) throws Exception {
            mockMvc.perform(get(AUTHENTICATE_API_ROOT)
                            .content(JsonStringUtil.asJsonString(request))
                            .contentType(MEDIA_TYPE_JSON)
                            .accept(MEDIA_TYPE_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(result -> assertEquals(
                            "Incorrect password !",
                            Objects.requireNonNull(result.getResolvedException()).getMessage()))
                    .andExpect(result -> assertTrue(
                            result.getResolvedException() instanceof BadCredentialsException));
        }
    }
}
