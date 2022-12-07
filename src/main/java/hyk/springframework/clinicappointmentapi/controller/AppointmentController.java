package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentUpdateStatusDTO;
import hyk.springframework.clinicappointmentapi.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> findAllAppointments() {
        return new ResponseEntity<>(appointmentService.findAllAppointments(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('DOCTOR') and " +
            "@doctorAuthorizationManger.doctorIdMatches(authentication, #doctorId))")
    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDTO>> findAllAppointmentsByDoctorId(@PathVariable Long doctorId) {
        return new ResponseEntity<>(appointmentService.findAllAppointmentsByDoctorId(doctorId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('PATIENT') and " +
            "@patientAuthorizationManger.patientIdMatches(authentication, #patientId))")
    @GetMapping("/patients/{patientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> findAllAppointmentsByPatientId(@PathVariable Long patientId) {
        return new ResponseEntity<>(appointmentService.findAllAppointmentsByPatientId(patientId), HttpStatus.OK);
    }

    @PostAuthorize("hasRole('ADMIN') or " +
            "(hasRole('PATIENT') and " +
            "@patientAuthorizationManger.patientIdMatches(authentication, returnObject.body.patientId)) or " +
            "(hasRole('DOCTOR') and " +
            "@doctorAuthorizationManger.doctorIdMatches(authentication, returnObject.body.doctorId))")
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> findAppointmentById(@PathVariable Long appointmentId) {
        AppointmentResponseDTO returnDto = appointmentService.findAppointmentById(appointmentId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> saveNewAppointment(@Valid @RequestBody AppointmentRegistrationDTO appointmentRegistrationDTO) {
        HttpHeaders headers = new HttpHeaders();
        AppointmentResponseDTO savedDto = appointmentService.saveNewAppointment(appointmentRegistrationDTO);
        headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/v1/appointments/{appointmentId}").buildAndExpand(savedDto.getId()).toUri());
        return new ResponseEntity<>(savedDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('DOCTOR') and " +
            "@appointmentAuthorizationManger.appointmentIdMatches(authentication, #appointmentId))")
    @PatchMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointmentStatus(@PathVariable Long appointmentId, @Valid @RequestBody AppointmentUpdateStatusDTO appointmentUpdateStatusDTO) {
        return new ResponseEntity<>(appointmentService.updateAppointmentStatus(appointmentId, appointmentUpdateStatusDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> deleteAppointmentById(@PathVariable Long appointmentId) {
        appointmentService.deleteAppointmentById(appointmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
