package hyk.springframework.clinicappointmentapi.web.controller.api.v1;

import hyk.springframework.clinicappointmentapi.service.ScheduleService;
import hyk.springframework.clinicappointmentapi.web.dto.ScheduleDTO;
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
@RequestMapping("/api/v1/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR', 'PATIENT')")
    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> showAllSchedules(
            @RequestParam(name = "doctorId", required = false) Long doctorId) {
        return new ResponseEntity<>(scheduleService.findAllSchedules(doctorId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR', 'PATIENT')")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDTO> showScheduleById(@PathVariable Long scheduleId) {
        ScheduleDTO returnDto = scheduleService.findScheduleById(scheduleId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO) {
        HttpHeaders headers = new HttpHeaders();
        ScheduleDTO savedDto = scheduleService.saveNewSchedule(scheduleDTO);
        headers.setLocation(UriComponentsBuilder.newInstance()
                .path("/api/v1/schedules/{scheduleId}").buildAndExpand(savedDto.getId()).toUri());
        return new ResponseEntity<>(savedDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping ("/{scheduleId}")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleDTO scheduleDTO) {
        return new ResponseEntity<>(scheduleService.updateSchedule(scheduleId, scheduleDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long scheduleId) {
        scheduleService.deleteScheduleById(scheduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}