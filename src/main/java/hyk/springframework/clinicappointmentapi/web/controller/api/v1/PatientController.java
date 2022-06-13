package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.service.PatientService;
import hyk.springframework.clinicappointmentapi.web.dto.PatientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/patients")
public class PatientController {
    private final PatientService patientService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping
    public ResponseEntity<List<PatientDTO>> showAllAppointments() {
        return new ResponseEntity<>(patientService.findAllPatients(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDTO> showPatientById(@PathVariable Long patientId) {
        PatientDTO returnDto = patientService.findPatientById(patientId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        HttpHeaders headers = new HttpHeaders();
        PatientDTO savedDto = patientService.saveNewPatient(patientDTO);
        headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/v1/patients/{patientId}").buildAndExpand(savedDto.getId()).toUri());
        return new ResponseEntity<>(savedDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    @PutMapping ("/{patientId}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long patientId, @Valid @RequestBody PatientDTO patientDTO) {
        return new ResponseEntity<>(patientService.updatePatient(patientId, patientDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long patientId) {
        patientService.deletePatientById(patientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
