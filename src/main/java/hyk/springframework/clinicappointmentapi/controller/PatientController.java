
package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.dto.patient.PatientRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientUpdateDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientResponseDTO;
import hyk.springframework.clinicappointmentapi.service.PatientService;
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
    public ResponseEntity<List<PatientResponseDTO>> findAllPatients() {
        return new ResponseEntity<>(patientService.findAllPatients(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponseDTO> findPatientById(@PathVariable Long patientId) {
        PatientResponseDTO returnDto = patientService.findPatientById(patientId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> registerPatient(@Valid @RequestBody PatientRegistrationDTO patientRegistrationDTO) {
        HttpHeaders headers = new HttpHeaders();
        PatientResponseDTO savedDto = patientService.saveNewPatient(patientRegistrationDTO);
        headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/v1/patients/{patientId}").buildAndExpand(savedDto.getId()).toUri());
        return new ResponseEntity<>(savedDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('PATIENT') and " +
            "@patientAuthorizationManger.patientIdMatches(authentication, #patientId))")
    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable Long patientId, @Valid @RequestBody PatientUpdateDTO patientUpdateDTO) {
        return new ResponseEntity<>(patientService.updatePatient(patientId, patientUpdateDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatientById(@PathVariable Long patientId) {
        patientService.deletePatientById(patientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
