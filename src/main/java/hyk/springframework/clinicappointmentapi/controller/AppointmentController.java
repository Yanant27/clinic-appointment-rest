package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentUpdateStatusDTO;
import hyk.springframework.clinicappointmentapi.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> findAllAppointments() {
        return new ResponseEntity<>(appointmentService.findAllAppointments(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> findAppointmentById(@PathVariable Long appointmentId) {
        AppointmentResponseDTO returnDto = appointmentService.findAppointmentById(appointmentId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> saveNewAppointment(@RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        AppointmentResponseDTO savedDto = appointmentService.saveNewAppointment(appointmentRequestDTO);
        headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/v1/appointments/{appointmentId}").buildAndExpand(savedDto.getId()).toUri());
        return new ResponseEntity<>(savedDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PatchMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointmentStatus(@PathVariable Long appointmentId, @RequestBody AppointmentUpdateStatusDTO appointmentUpdateStatusDTO) {
        return new ResponseEntity<>(appointmentService.updateAppointmentStatus(appointmentId, appointmentUpdateStatusDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> deleteAppointmentById(@PathVariable Long appointmentId) {
        appointmentService.deleteAppointmentById(appointmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDTO>> findAllAppointmentsByDoctorId(@PathVariable Long doctorId) {
        return new ResponseEntity<>(appointmentService.findAllAppointmentsByDoctorId(doctorId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    @GetMapping("/patients/{patientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> findAllAppointmentsByPatientId(@PathVariable Long patientId) {
        return new ResponseEntity<>(appointmentService.findAllAppointmentsByPatientId(patientId), HttpStatus.OK);
    }
}
