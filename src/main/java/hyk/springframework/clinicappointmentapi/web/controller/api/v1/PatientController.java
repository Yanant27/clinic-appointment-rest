package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.service.PatientService;
import hyk.springframework.clinicappointmentapi.web.dto.PatientDTO;
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
@RequestMapping("/api/v1/patients")
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<PatientDTO>> showAllAppointments() {
        return new ResponseEntity<>(patientService.findAllPatients(), HttpStatus.OK);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDTO> showPatientById(@PathVariable Long patientId) {
        PatientDTO returnDto = patientService.findPatientById(patientId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
        HttpHeaders headers = new HttpHeaders();
        PatientDTO savedDto = patientService.saveNewPatient(patientDTO);
        headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/v1/Patients/{id}").buildAndExpand(savedDto.getId()).toUri());
        return new ResponseEntity<>(savedDto, headers, HttpStatus.CREATED);

    }

    @PutMapping ("/{patientId}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long patientId, @RequestBody PatientDTO patientDTO) {
        return new ResponseEntity<>(patientService.updatePatient(patientId, patientDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long patientId) {
        patientService.deletePatientById(patientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
