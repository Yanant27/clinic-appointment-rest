package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.dto.appointment.AppointmentRegistrationDTO;
import hyk.springframework.clinicappointmentapi.exception.ResourceNotFoundException;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Htoo Yanant Khin
 **/
public abstract class AppointmentMapperDecorator implements AppointmentMapper {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    @Qualifier("delegate")
    private AppointmentMapper appointmentMapper;

    @Override
    public Appointment appointmentRegistrationDtoToAppointment(AppointmentRegistrationDTO appointmentRegistrationDTO) {
        Appointment appointment = appointmentMapper.appointmentRegistrationDtoToAppointment(appointmentRegistrationDTO);
        // Update data
        appointment.setDoctor(doctorRepository.findById(appointmentRegistrationDTO.getDoctorId())
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Doctor Not Found. ID: " + appointmentRegistrationDTO.getDoctorId());
                }));
        appointment.setPatient(patientRepository.findById(appointmentRegistrationDTO.getPatientId())
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Patient Not Found. ID: " + appointmentRegistrationDTO.getPatientId());
                }));
        return appointment;
    }
}
