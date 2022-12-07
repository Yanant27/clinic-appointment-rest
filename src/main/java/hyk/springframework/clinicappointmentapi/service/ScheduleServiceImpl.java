package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.dto.mapper.ScheduleMapper;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleRegistrationDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.schedule.ScheduleUpdateDTO;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import hyk.springframework.clinicappointmentapi.util.LoginUserUtil;
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

    private final DoctorRepository doctorRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    public List<ScheduleResponseDTO> findAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(scheduleMapper::scheduleToScheduleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponseDTO> findAllSchedulesByDoctorId(Long doctorId) {
        return scheduleRepository.findAllByDoctorId(doctorId).stream()
                .map(scheduleMapper::scheduleToScheduleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDTO findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .map(scheduleMapper::scheduleToScheduleResponseDto)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Schedule Not Found. ID: " + scheduleId);
                });
    }

    @Override
    public ScheduleResponseDTO saveNewSchedule(ScheduleRegistrationDTO scheduleRegistrationDTO) {
        Schedule schedule = scheduleMapper.scheduleRegistrationDtoToSchedule(scheduleRegistrationDTO);
        schedule.setCreatedBy(LoginUserUtil.getLoginUsername());
        schedule.setModifiedBy(LoginUserUtil.getLoginUsername());
        return scheduleMapper.scheduleToScheduleResponseDto(scheduleRepository.save(schedule));
    }

    @Override
    public ScheduleResponseDTO updateSchedule(Long scheduleId, ScheduleUpdateDTO scheduleUpdateDTO) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> {
                    throw new ResourceNotFoundException("Schedule Not Found. ID: " + scheduleId);
                });
        Doctor doctor = doctorRepository.findById(scheduleUpdateDTO.getDoctorId())
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Doctor Not Found. ID: " + scheduleUpdateDTO.getDoctorId());
                });

        schedule.setDayOfWeek(scheduleUpdateDTO.getDayOfWeek());
        schedule.setTimeslot(scheduleUpdateDTO.getTimeslot());
        schedule.setDoctor(doctor);
        schedule.setModifiedBy(LoginUserUtil.getLoginUsername());
        return scheduleMapper.scheduleToScheduleResponseDto(scheduleRepository.save(schedule));
    }

    @Override
    public void deleteScheduleById(Long scheduleId) {
        scheduleRepository.findById(scheduleId)
                .ifPresentOrElse(appointment -> {
                    scheduleRepository.deleteById(scheduleId);
                }, () -> {
                    throw new ResourceNotFoundException("Schedule Not Found. ID: " + scheduleId);
                });
    }
}
