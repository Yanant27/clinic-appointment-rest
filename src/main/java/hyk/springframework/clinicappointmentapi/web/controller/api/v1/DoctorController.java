package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.service.DoctorService;
import hyk.springframework.clinicappointmentapi.web.dto.DoctorDTO;
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
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> showAllAppointments() {
        return new ResponseEntity<>(doctorService.findAllDoctors(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorDTO> showDoctorById(@PathVariable Long doctorId) {
        DoctorDTO returnDto = doctorService.findDoctorById(doctorId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO DoctorDTO) {
        HttpHeaders headers = new HttpHeaders();
        DoctorDTO savedDto = doctorService.saveNewDoctor(DoctorDTO);
        headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/v1/doctors/{doctorId}").buildAndExpand(savedDto.getId()).toUri());
        return new ResponseEntity<>(savedDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping ("/{doctorId}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long doctorId, @Valid @RequestBody DoctorDTO DoctorDTO) {
        return new ResponseEntity<>(doctorService.updateDoctor(doctorId, DoctorDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctorById(doctorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
