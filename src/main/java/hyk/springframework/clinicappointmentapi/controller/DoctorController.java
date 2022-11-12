package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorResponseDTO;
import hyk.springframework.clinicappointmentapi.service.DoctorService;
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
    public ResponseEntity<List<DoctorResponseDTO>> findAllDoctors() {
        return new ResponseEntity<>(doctorService.findAllDoctors(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponseDTO> findDoctorById(@PathVariable Long doctorId) {
        DoctorResponseDTO returnDto = doctorService.findDoctorById(doctorId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PostMapping
    public ResponseEntity<DoctorResponseDTO> saveNewDoctor(@Valid @RequestBody DoctorRequestDTO doctorRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        DoctorResponseDTO savedDto = doctorService.saveNewDoctor(doctorRequestDTO);
        headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/v1/doctors/{doctorId}").buildAndExpand(savedDto.getId()).toUri());
        return new ResponseEntity<>(savedDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable Long doctorId, @Valid @RequestBody DoctorRequestDTO doctorRequestDTO) {
        return new ResponseEntity<>(doctorService.updateDoctor(doctorId, doctorRequestDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctorById(@PathVariable Long doctorId) {
        doctorService.deleteDoctorById(doctorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/specializations/{specialization}")
    public ResponseEntity<List<DoctorResponseDTO>> findAllDoctorsBySpecialization(@PathVariable String specialization) {
        return new ResponseEntity<>(doctorService.findAllDoctorsBySpecialization(specialization), HttpStatus.OK);
    }
}
