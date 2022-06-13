package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.service.AppointmentService;
import hyk.springframework.clinicappointmentapi.web.dto.AppointmentDTO;
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
    public ResponseEntity<List<AppointmentDTO>> showAllAppointments(
            @RequestParam(name = "doctorId", required = false) Long doctorId,
            @RequestParam(name = "patientId", required = false) Long patientId,
            @RequestParam(name = "scheduleId", required = false) Long scheduleId) {
        return new ResponseEntity<>(appointmentService.findAllAppointments(doctorId, patientId, scheduleId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDTO> showAppointmentById(@PathVariable Long appointmentId) {
        AppointmentDTO returnDto = appointmentService.findAppointmentById(appointmentId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        HttpHeaders headers = new HttpHeaders();
        AppointmentDTO savedDto = appointmentService.saveAppointment(appointmentDTO);
        headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/v1/appointments/{appointmentId}").buildAndExpand(savedDto.getAppointmentId()).toUri());
        return new ResponseEntity<>(savedDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PatchMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDTO> updateAppointmentStatus(@PathVariable Long appointmentId, @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO savedDto = appointmentService.findAppointmentById(appointmentId);
        savedDto.setAppointmentStatus(appointmentDTO.getAppointmentStatus());
        AppointmentDTO returnDto = appointmentService.saveAppointment(savedDto);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long appointmentId) {
        appointmentService.deleteAppointmentById(appointmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
