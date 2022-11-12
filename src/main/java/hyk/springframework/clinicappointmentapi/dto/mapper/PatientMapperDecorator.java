package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientRequestDTO;
import hyk.springframework.clinicappointmentapi.dto.patient.PatientResponseDTO;
import hyk.springframework.clinicappointmentapi.exception.NotFoundException;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
public class PatientMapperDecorator implements PatientMapper {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    @Qualifier("delegate")
    private AppointmentMapper appointmentMapper;

    @Autowired
    @Qualifier("delegate")
    private PatientMapper patientMapper;

    @Override
    public Patient patientRequestDtoToPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRequestDTO == null) {
            return null;
        }
        Patient patient;
        if (patientRequestDTO.getId() != null) {
            // Update existing patient case
            patient = patientRepository.findById(patientRequestDTO.getId())
                    .orElseThrow(() -> {
                        throw new NotFoundException("Patient Not Found. ID: " + patientRequestDTO.getId());
                    });
        } else {
            // Register new patient case
            patient = patientMapper.patientRequestDtoToPatient(patientRequestDTO);
        }
        // Update data
        patient.setName(patientRequestDTO.getName());
        patient.setAge(patientRequestDTO.getAge());
        patient.setGender(patientRequestDTO.getGender());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setPhoneNumber(patientRequestDTO.getPhoneNumber());
        return patient;
    }

    @Override
    public PatientResponseDTO patientToPatientResponseDto(Patient patient) {
        if (patient == null) {
            return null;
        }
        PatientResponseDTO patientResponseDTO = patientMapper.patientToPatientResponseDto(patient);
        patientResponseDTO.setId(patient.getId());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setAge(patient.getAge());
        patientResponseDTO.setGender(patient.getGender());
        patientResponseDTO.setAddress(patient.getAddress());
        patientResponseDTO.setPhoneNumber(patient.getPhoneNumber());
        if (patient.getAppointments() != null) {
            patientResponseDTO.setAppointments(patient.getAppointments().stream()
                    .map(appointment -> appointmentMapper.appointmentToAppointmentResponseDto(appointment))
                    .collect(Collectors.toList()));
        }
        return patientResponseDTO;
    }

    @Override
    public PatientRequestDTO patientToPatientRequestDto(Patient patient) {
        if (patient == null) {
            return null;
        }
        PatientRequestDTO patientRequestDTO = patientMapper.patientToPatientResponseDto(patient);
        patientRequestDTO.setId(patient.getId());
        patientRequestDTO.setName(patient.getName());
        patientRequestDTO.setAge(patient.getAge());
        patientRequestDTO.setGender(patient.getGender());
        patientRequestDTO.setAddress(patient.getAddress());
        patientRequestDTO.setPhoneNumber(patient.getPhoneNumber());
        return patientRequestDTO;
    }
}
