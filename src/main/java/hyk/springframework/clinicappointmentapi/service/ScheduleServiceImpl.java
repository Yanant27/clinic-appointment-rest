package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import hyk.springframework.clinicappointmentapi.web.dto.ScheduleDTO;
import hyk.springframework.clinicappointmentapi.web.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    public List<ScheduleDTO> findAllSchedules(Long doctorId) {
        List<Schedule> schedules;
        if (doctorId != null) {
            schedules = scheduleRepository.findAllByDoctorId(doctorId);
        } else {
            schedules = scheduleRepository.findAll();
        }
        return schedules.stream()
                .map(scheduleMapper::scheduleToScheduleDto)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleDTO findScheduleById(Long scheduleId) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
        if (optionalSchedule.isPresent()) {
            return scheduleMapper.scheduleToScheduleDto(optionalSchedule.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule Not Found. ID: " + scheduleId);
        }
    }

    @Override
    public ScheduleDTO saveNewSchedule(ScheduleDTO scheduleDTO) {
        return scheduleMapper.scheduleToScheduleDto(
                scheduleRepository.save(scheduleMapper.scheduleDtoToSchedule(scheduleDTO)));
    }

    @Override
    public ScheduleDTO updateSchedule(Long scheduleId, ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule Not Found. ID: " + scheduleId);
        });
        Doctor doctor = doctorRepository.findById(scheduleDTO.getDoctorId()).orElseThrow(RuntimeException::new);
        doctor.setName(scheduleDTO.getDoctorName());

        schedule.setDate(scheduleDTO.getDate());
        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setEndTime(scheduleDTO.getEndTime());
        schedule.setDoctor(doctor);
        schedule.setDoctorStatus(scheduleDTO.getDoctorStatus());
        return scheduleMapper.scheduleToScheduleDto(scheduleRepository.save(schedule));
    }

    @Override
    public void deleteScheduleById(Long scheduleId) {
        scheduleRepository.findById(scheduleId).ifPresentOrElse(appointment -> {
            scheduleRepository.deleteById(scheduleId);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule Not Found. ID: " + scheduleId);
        });
    }
}
