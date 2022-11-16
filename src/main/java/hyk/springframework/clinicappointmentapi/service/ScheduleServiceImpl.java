package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.dto.mapper.ScheduleMapper;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import hyk.springframework.clinicappointmentapi.enums.ScheduleStatus;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    private final AppointmentRepository appointmentRepository;

    private final DoctorRepository doctorRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    public List<ScheduleResponseDTO> findAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(scheduleMapper::scheduleToScheduleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDTO findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .map(scheduleMapper::scheduleToScheduleResponseDto)
                .orElseThrow(() -> {
                    throw new NotFoundException("Schedule Not Found. ID: " + scheduleId);
                });
    }

    @Override
    public ScheduleResponseDTO saveNewSchedule(ScheduleRequestDTO scheduleRequestDTO) {
        // Set default schedule status for new schedule
        scheduleRequestDTO.setScheduleStatus(ScheduleStatus.AVAILABLE);
        return scheduleMapper.scheduleToScheduleResponseDto
                (scheduleRepository.save(scheduleMapper.scheduleRequestDtoToSchedule(scheduleRequestDTO)));
    }

    @Override
    public ScheduleResponseDTO updateSchedule(Long scheduleId, ScheduleRequestDTO scheduleRequestDTO) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Schedule Not Found. ID: " + scheduleId);
                });
        Doctor doctor = doctorRepository.findById(scheduleRequestDTO.getDoctorId())
                .orElseThrow(() -> {
                    throw new NotFoundException("Doctor Not Found. ID: " + scheduleRequestDTO.getDoctorId());
                });

        schedule.setDayOfWeek(scheduleRequestDTO.getDayOfWeek());
        schedule.setTimeslot(scheduleRequestDTO.getTimeslot());
        schedule.setScheduleStatus(scheduleRequestDTO.getScheduleStatus());
        schedule.setDoctor(doctor);
        return scheduleMapper.scheduleToScheduleResponseDto(scheduleRepository.save(schedule));
    }

    @Override
    public void deleteScheduleById(Long scheduleId) {
        scheduleRepository.findById(scheduleId)
                .ifPresentOrElse(appointment -> {
                    scheduleRepository.deleteById(scheduleId);
                }, () -> {
                    throw new NotFoundException("Schedule Not Found. ID: " + scheduleId);
                });
    }

    @Override
    public List<ScheduleResponseDTO> findAllSchedulesByDoctorId(Long doctorId) {
        return scheduleRepository.findAllByDoctorId(doctorId).stream()
                .map(scheduleMapper::scheduleToScheduleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void logicalDeleteScheduleByDoctorId(Long scheduleId, Long doctorId) {
        scheduleRepository.findByIdAndDoctorId(scheduleId, doctorId)
                .ifPresentOrElse(schedule -> {
                    // Logically delete (set null) doctor
                    schedule.setDoctor(null);
                    scheduleRepository.save(schedule);
                    // Delete all appointments booked at this schedule
                    appointmentRepository.deleteAll(appointmentRepository.findAllByScheduleId(scheduleId));
                }, () -> {
                    throw new NotFoundException("Schedule Not Found. Schedule ID: " + scheduleId
                    + ", Doctor ID: " + doctorId);
                });
    }
}
