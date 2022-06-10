package hyk.springframework.clinicappointmentapi.service;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.web.dto.AppointmentDTO;
import hyk.springframework.clinicappointmentapi.web.mapper.AppointmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public List<AppointmentDTO> findAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::appointmentToAppointmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDTO findAppointmentById(Long appointmentId) {
        Optional<Appointment> optionalAppointmentDTO = appointmentRepository.findById(appointmentId);
        if (optionalAppointmentDTO.isPresent()) {
            return appointmentMapper.appointmentToAppointmentDto(optionalAppointmentDTO.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment Not Found. ID: " + appointmentId);
        }
    }

    @Override
    @Transactional
    public AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO) {
        return appointmentMapper.appointmentToAppointmentDto(
                appointmentRepository.save(appointmentMapper.appointmentDtoToAppointment(appointmentDTO)));
    }

    @Override
    @Transactional
    public void deleteAppointmentById(Long appointmentId) {
        appointmentRepository.findById(appointmentId).ifPresentOrElse(appointment -> {
            appointmentRepository.deleteById(appointmentId);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment Not Found. ID: " + appointmentId);
        });
    }
}
