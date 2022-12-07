package hyk.springframework.clinicappointmentapi.controller;

import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleUpdateDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import hyk.springframework.clinicappointmentapi.service.ScheduleService;
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
@RequestMapping("/api/v1/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR', 'PATIENT')")
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDTO>> findAllSchedules() {
        return new ResponseEntity<>(scheduleService.findAllSchedules(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR', 'PATIENT')")
    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<List<ScheduleResponseDTO>> findAllSchedulesByDoctorId(@PathVariable Long doctorId) {
        return new ResponseEntity<>(scheduleService.findAllSchedulesByDoctorId(doctorId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR', 'PATIENT')")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDTO> findScheduleById(@PathVariable Long scheduleId) {
        ScheduleResponseDTO returnDto = scheduleService.findScheduleById(scheduleId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('DOCTOR') and " +
            "@doctorAuthorizationManger.doctorIdMatches(authentication, #scheduleRegistrationDTO.doctorId))")
    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> saveNewSchedule(@Valid @RequestBody ScheduleRegistrationDTO scheduleRegistrationDTO) {
        HttpHeaders headers = new HttpHeaders();
        ScheduleResponseDTO savedDto = scheduleService.saveNewSchedule(scheduleRegistrationDTO);
        headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/v1/schedules/{scheduleId}").buildAndExpand(savedDto.getId()).toUri());
        return new ResponseEntity<>(savedDto, headers, HttpStatus.CREATED);
    }

    @PostAuthorize("hasRole('ADMIN') or " +
            "(hasRole('DOCTOR') and " +
            "@scheduleAuthorizationManger.scheduleIdMatches(authentication, #scheduleId))")
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDTO> updateSchedule(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleUpdateDTO scheduleUpdateDTO) {
        return new ResponseEntity<>(scheduleService.updateSchedule(scheduleId, scheduleUpdateDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('DOCTOR') and " +
            "@scheduleAuthorizationManger.scheduleIdMatches(authentication, #scheduleId))")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteScheduleById(@PathVariable Long scheduleId) {
        scheduleService.deleteScheduleById(scheduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}