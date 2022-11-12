package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentResponseDTO;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentUpdateStatusDTO;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Htoo Yanant Khin
 **/
public abstract class AppointmentMapperDecorator implements AppointmentMapper {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    @Qualifier("delegate")
    private AppointmentMapper appointmentMapper;

    @Override
    public Appointment appointmentRequestDtoToAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        if (appointmentRequestDTO == null) {
            return null;
        }
        Appointment appointment;
        if (appointmentRequestDTO.getId() != null) {
            // Create new appointment case
            appointment = appointmentRepository.findById(appointmentRequestDTO.getId())
                    .orElseThrow(() -> {
                        throw new NotFoundException("Appointment Not Found. ID: " + appointmentRequestDTO.getId());
                    });
        } else {
            // Update existing appointment case
            appointment = appointmentMapper.appointmentRequestDtoToAppointment(appointmentRequestDTO);
        }
        // Update data
        appointment.setDoctor(doctorRepository.findById(appointmentRequestDTO.getDoctorId())
                .orElseThrow(() -> {
                    throw new NotFoundException("Doctor Not Found. ID: " + appointmentRequestDTO.getDoctorId());
                }));
        appointment.setPatient(patientRepository.findById(appointmentRequestDTO.getPatientRequestDTO().getId())
                .orElseThrow(() -> {
                    throw new NotFoundException("Patient Not Found. ID: " + appointmentRequestDTO.getPatientRequestDTO().getId());
                }));
        appointment.setSchedule(scheduleRepository.findById(appointmentRequestDTO.getScheduleId())
                .orElseThrow(() -> {
                    throw new NotFoundException("Schedule Not Found. ID: " + appointmentRequestDTO.getScheduleId());
                }));
        appointment.setAppointmentDate(appointmentRequestDTO.getAppointmentDate());
        appointment.setAppointmentStatus(appointmentRequestDTO.getAppointmentStatus());
        appointment.setCreator(appointmentRequestDTO.getCreator());
        return appointment;
    }

    @Override
    public AppointmentResponseDTO appointmentToAppointmentResponseDto(Appointment appointment) {
        AppointmentResponseDTO appointmentResponseDTO = appointmentMapper.appointmentToAppointmentResponseDto(appointment);
        appointmentResponseDTO.setId(appointment.getId());
        appointmentResponseDTO.setDoctorId(appointment.getDoctor().getId());
        appointmentResponseDTO.setDoctorName(appointment.getDoctor().getName());
        appointmentResponseDTO.setSpecialization(appointment.getDoctor().getSpecialization());
        appointmentResponseDTO.setPatientId(appointment.getPatient().getId());
        appointmentResponseDTO.setPatientName(appointment.getPatient().getName());
        appointmentResponseDTO.setPatientPhoneNumber(appointment.getPatient().getPhoneNumber());
        appointmentResponseDTO.setAppointmentDate(appointment.getAppointmentDate());
        appointmentResponseDTO.setAppointmentStatus(appointment.getAppointmentStatus());
        appointmentResponseDTO.setScheduleId(appointment.getSchedule().getId());
        appointmentResponseDTO.setTimeslot(appointment.getSchedule().getTimeslot());
        return appointmentResponseDTO;
    }

    @Override
    public Appointment AppointmentUpdateStatusDtoToAppointment(AppointmentUpdateStatusDTO appointmentUpdateStatusDTO) {
        if (appointmentUpdateStatusDTO == null) {
            return null;
        }
        Appointment appointment;
        if (appointmentUpdateStatusDTO.getId() != null) {
            // Create new appointment case
            appointment = appointmentRepository.findById(appointmentUpdateStatusDTO.getId())
                    .orElseThrow(() -> {
                        throw new NotFoundException("Appointment Not Found. ID: " + appointmentUpdateStatusDTO.getId());
                    });
        } else {
            // Update existing appointment case
            appointment = appointmentMapper.AppointmentUpdateStatusDtoToAppointment(appointmentUpdateStatusDTO);
        }
        // Update data
        appointment.setAppointmentStatus(appointmentUpdateStatusDTO.getAppointmentStatus());
        return appointment;
    }
}
