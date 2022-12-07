package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.doctor.DoctorUpdateDTO;
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
    @GetMapping("/specializations/{specialization}")
    public ResponseEntity<List<DoctorResponseDTO>> findAllDoctorsBySpecialization(@PathVariable String specialization) {
        return new ResponseEntity<>(doctorService.findAllDoctorsBySpecialization(specialization), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponseDTO> findDoctorById(@PathVariable Long doctorId) {
        DoctorResponseDTO returnDto = doctorService.findDoctorById(doctorId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DoctorResponseDTO> registerDoctor(@Valid @RequestBody DoctorRegistrationDTO doctorRegistrationDTO) {
        HttpHeaders headers = new HttpHeaders();
        DoctorResponseDTO savedDto = doctorService.saveNewDoctor(doctorRegistrationDTO);
        headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/v1/doctors/{doctorId}").buildAndExpand(savedDto.getId()).toUri());
        return new ResponseEntity<>(savedDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('DOCTOR') and " +
            "@doctorAuthorizationManger.doctorIdMatches(authentication, #doctorId))")
    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable Long doctorId, @Valid @RequestBody DoctorUpdateDTO doctorUpdateDTO) {
        return new ResponseEntity<>(doctorService.updateDoctor(doctorId, doctorUpdateDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctorById(@PathVariable Long doctorId) {
        doctorService.deleteDoctorById(doctorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
