package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.dto.security.UserAuthenticationDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;


/**
 * @author Htoo Yanant Khin
 **/
public abstract class BaseIT {
    final MediaType MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mockMvc;

    private static final String ADMIN = "Admin Role";
    private static final String DOCTOR = "Doctor Role";
    private static final String PATIENT = "Patient Role";

    private static final String JWT_ADMIN = generateJwt("admin", "ROLE_ADMIN");
    private static final String JWT_DOCTOR = generateJwt("linhtet", "ROLE_DOCTOR");
    private static final String JWT_PATIENT = generateJwt("hsuhsu", "ROLE_PATIENT");

    public static Stream<Arguments> getAdmin() {
        return Stream.of(Arguments.of(ADMIN, JWT_ADMIN));
    }

    public static Stream<Arguments> getAllUsers() {
        return Stream.of(
                Arguments.of(ADMIN, JWT_ADMIN),
                Arguments.of(DOCTOR, JWT_DOCTOR), // doctor
                Arguments.of(PATIENT, JWT_PATIENT)); // patient
    }

    public static Stream<Arguments> getAdminDoctor() {
        return Stream.of(
                Arguments.of(ADMIN, JWT_ADMIN),
                Arguments.of(DOCTOR, JWT_DOCTOR)); // doctor
    }

    public static Stream<Arguments> getAdminPatient() {
        return Stream.of(
                Arguments.of(ADMIN, JWT_ADMIN),
                Arguments.of(PATIENT, JWT_PATIENT)); // patient
    }

    public static Stream<Arguments> getDoctorPatient() {
        return Stream.of(
                Arguments.of(DOCTOR, JWT_DOCTOR), // doctor
                Arguments.of(PATIENT, JWT_PATIENT)); // patient
    }

    public static Stream<Arguments> getDoctor() {
        return Stream.of(
                Arguments.of(DOCTOR, JWT_DOCTOR)); // doctor
    }

    public static Stream<Arguments> getUnmatchedDoctor() {
        return Stream.of(
                Arguments.of(DOCTOR, generateJwt("nyihtut", "ROLE_DOCTOR"))); // doctor
    }

    public static Stream<Arguments> getPatient() {
        return Stream.of(
                Arguments.of(PATIENT, JWT_PATIENT)); // patient
    }

    public static Stream<Arguments> getUnmatchedPatient() {
        return Stream.of(
                Arguments.of(PATIENT, generateJwt("aungaung", "ROLE_PATIENT"))); // patient
    }

    public static Stream<Arguments> getAuthenticatedUsers() {
        return Stream.of(
                Arguments.of("Admin", UserAuthenticationDTO.builder().username("admin").password("admin").build()),
                Arguments.of("Admin", UserAuthenticationDTO.builder().username("linhtet").password("password").build()),
                Arguments.of("Admin", UserAuthenticationDTO.builder().username("hsuhsu").password("password").build()));
    }

    public static Stream<Arguments> getUserWithInvalidUsername() {
        return Stream.of(
                Arguments.of("Admin", UserAuthenticationDTO.builder().username("guest").password("admin").build()));
    }

    public static Stream<Arguments> getUserWithIncorrectPassword() {
        return Stream.of(
                Arguments.of("Admin", UserAuthenticationDTO.builder().username("admin").password("invalidpassword").build()));
    }

    public static String generateJwt(String username, String authorities) {
        SecretKey key = Keys.hmacShaKeyFor("hfgry463hf746hf573ydh475fhys1111".getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setIssuer("Clinic Appointment API")
                .setSubject("JWT Token")
                .claim("username", username)
                .claim("authorities", authorities)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 30000000))
                .signWith(key)
                .compact();
    }
}
