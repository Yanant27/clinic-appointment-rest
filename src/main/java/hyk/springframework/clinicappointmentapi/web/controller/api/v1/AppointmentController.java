package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.service.AppointmentService;
import hyk.springframework.clinicappointmentapi.web.dto.AppointmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<AppointmentDTO>> showAllAppointments() {
        return new ResponseEntity<>(appointmentService.findAllAppointments(), HttpStatus.OK);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDTO> showAppointmentById(@PathVariable Long appointmentId) {
        AppointmentDTO returnDto = appointmentService.findAppointmentById(appointmentId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        HttpHeaders headers = new HttpHeaders();
        AppointmentDTO savedDto = appointmentService.saveAppointment(appointmentDTO);
        headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/v1/appointments/{id}").buildAndExpand(savedDto.getAppointmentId()).toUri());
        return new ResponseEntity<>(savedDto, headers, HttpStatus.CREATED);

    }

    @PatchMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDTO> updateAppointmentStatus(@PathVariable Long appointmentId, @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO savedDto = appointmentService.findAppointmentById(appointmentId);
        savedDto.setAppointmentStatus(appointmentDTO.getAppointmentStatus());
        return new ResponseEntity<>(appointmentService.saveAppointment(savedDto), HttpStatus.OK);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long appointmentId) {
        appointmentService.deleteAppointmentById(appointmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
