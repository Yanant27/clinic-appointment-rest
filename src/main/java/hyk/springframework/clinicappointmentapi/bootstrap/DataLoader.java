package hyk.springframework.clinicappointmentapi.bootstrap;

import hyk.springframework.clinicappointmentapi.domain.Appointment;
import hyk.springframework.clinicappointmentapi.domain.Doctor;
import hyk.springframework.clinicappointmentapi.domain.Patient;
import hyk.springframework.clinicappointmentapi.domain.Schedule;
import hyk.springframework.clinicappointmentapi.domain.security.Role;
import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.enums.AppointmentStatus;
import hyk.springframework.clinicappointmentapi.enums.Gender;
import hyk.springframework.clinicappointmentapi.repository.AppointmentRepository;
import hyk.springframework.clinicappointmentapi.repository.DoctorRepository;
import hyk.springframework.clinicappointmentapi.repository.PatientRepository;
import hyk.springframework.clinicappointmentapi.repository.ScheduleRepository;
import hyk.springframework.clinicappointmentapi.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Htoo Yanant Khin
 **/
@Slf4j
@RequiredArgsConstructor
@Component
@Profile({"test", "dev"})
public class DataLoader implements CommandLineRunner {

    private final ScheduleRepository scheduleRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final AppointmentRepository appointmentRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {
        Role roleDoctor = Role.builder().name("DOCTOR").build();
        Role rolePatient = Role.builder().name("PATIENT").build();

        userRepository.save(User.builder().username("admin").password(encoder.encode("admin")).role(Role.builder().name("ADMIN").build()).build());

        Doctor doctor1 = Doctor.builder()
                .name("Dr. Lin Htet")
                .dateOfBirth(LocalDate.of(1990,1,1))
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.1, 1st street, Than Lyin")
                .qualifications("MBBS")
                .specialization("Internal medicine")
                .user(User.builder().username("linhtet").password(encoder.encode("password")).role(roleDoctor).build())
                .createdBy("admin")
                .modifiedBy("admin")
                .build();
        Doctor doctor2 = Doctor.builder()
                .name("Dr. Nyi Htut")
                .dateOfBirth(LocalDate.of(1987, 1,1))
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.2, 2nd street, Hlaing Township, Yangon")
                .qualifications("MBBS")
                .specialization("Cardiology")
                .user(User.builder().username("nyihtut").password(encoder.encode("password")).role(roleDoctor).build())
                .createdBy("admin")
                .modifiedBy("admin")
                .build();
        Doctor doctor3 = Doctor.builder()
                .name("Dr. Shine Min")
                .dateOfBirth(LocalDate.of(1984, 1,1))
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.3, 3rd street, Lan Ma Taw Township, Yangon")
                .qualifications("MBBS")
                .specialization("General Surgery")
                .user(User.builder().username("shinemin").password(encoder.encode("password")).role(roleDoctor).build())
                .createdBy("admin")
                .modifiedBy("admin")
                .build();
        Doctor doctor4 = Doctor.builder()
                .name("Dr. Min Khant")
                .dateOfBirth(LocalDate.of(1987, 1,1))
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.4, 4th street, Tar Mwe Township, Yangon")
                .qualifications("MBBS")
                .specialization("General Surgery")
                .user(User.builder().username("minkhant").password(encoder.encode("password")).role(roleDoctor).build())
                .createdBy("admin")
                .modifiedBy("admin")
                .build();
        Doctor doctor5 = Doctor.builder()
                .name("Dr. Kyaw Swar")
                .dateOfBirth(LocalDate.of(1982, 1,1))
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.5, 5th street, Yan Kin Township, Yangon")
                .qualifications("MBBS")
                .specialization("Internal medicine")
                .user(User.builder().username("kyawswar").password(encoder.encode("password")).role(roleDoctor).build())
                .createdBy("kyawswar")
                .modifiedBy("kyawswar")
                .build();
        Doctor doctor6 = Doctor.builder()
                .name("Dr. Nay Oo")
                .dateOfBirth(LocalDate.of(1983, 1,1))
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.6, 6th street, San Chaung Township, Yangon")
                .qualifications("MBBS")
                .specialization("Cardiology")
                .user(User.builder().username("nayoo").password(encoder.encode("password")).role(roleDoctor).build())
                .createdBy("nayoo")
                .modifiedBy("nayoo")
                .build();
        Doctor doctor7 = Doctor.builder()
                .name("Dr. Aung Ko")
                .dateOfBirth(LocalDate.of(1981, 1,1))
                .gender(Gender.MALE)
                .phoneNumber("09111111111")
                .address("No.7, 7th street, Tha Mine Township, Yangon")
                .qualifications("MBBS")
                .specialization("Internal medicine")
                .user(User.builder().username("aungko").password(encoder.encode("password")).role(roleDoctor).build())
                .createdBy("aungko")
                .modifiedBy("aungko")
                .build();
        doctorRepository.saveAll(List.of(doctor1, doctor2, doctor3, doctor4, doctor5, doctor6, doctor7));

        Schedule schedule1 = Schedule.builder()
                .dayOfWeek(DayOfWeek.SUNDAY.name())
                .timeslot("09:00 ~ 10:00")
                .doctor(doctor1)
                .createdBy("admin")
                .modifiedBy("admin")
                .build();
        Schedule schedule2 = Schedule.builder()
                .dayOfWeek(DayOfWeek.MONDAY.name())
                .timeslot("09:00 ~ 10:00")
                .doctor(doctor1)
                .createdBy("linhtet")
                .modifiedBy("linihtet")
                .build();
        Schedule schedule3 = Schedule.builder()
                .dayOfWeek(DayOfWeek.MONDAY.name())
                .timeslot("10:00 ~ 11:00")
                .doctor(doctor2)
                .createdBy("admin")
                .modifiedBy("admin")
                .build();
        Schedule schedule4 = Schedule.builder()
                .dayOfWeek(DayOfWeek.THURSDAY.name())
                .timeslot("12:00 ~ 13:00")
                .doctor(doctor3)
                .createdBy("admin")
                .modifiedBy("admin")
                .build();
        Schedule schedule5 = Schedule.builder()
                .dayOfWeek(DayOfWeek.WEDNESDAY.name())
                .timeslot("13:00 ~ 14:00")
                .doctor(doctor4)
                .createdBy("admin")
                .modifiedBy("admin")
                .build();
        Schedule schedule6 = Schedule.builder()
                .dayOfWeek(DayOfWeek.TUESDAY.name())
                .timeslot("15:00 ~ 16:00")
                .doctor(doctor4)
                .createdBy("admin")
                .modifiedBy("admin")
                .build();
        Schedule schedule7 = Schedule.builder()
                .dayOfWeek(DayOfWeek.FRIDAY.name())
                .timeslot("17:00 ~ 18:00")
                .doctor(doctor5)
                .createdBy("kyawswar")
                .modifiedBy("kyawswar")
                .build();
        Schedule schedule8 = Schedule.builder()
                .dayOfWeek(DayOfWeek.SATURDAY.name())
                .timeslot("19:00 ~ 20:00")
                .doctor(doctor6)
                .createdBy("nayoo")
                .modifiedBy("nayoo")
                .build();
        Schedule schedule9 = Schedule.builder()
                .dayOfWeek(DayOfWeek.SUNDAY.name())
                .timeslot("12:00 ~ 13:00")
                .doctor(doctor7)
                .createdBy("aungko")
                .modifiedBy("aungko")
                .build();
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6,
                schedule7, schedule8, schedule9));

        Patient patient1 = Patient.builder()
                .name("Hsu Hsu")
                .dateOfBirth(LocalDate.of(1998, 1,1))
                .gender(Gender.FEMALE)
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .user(User.builder().username("hsuhsu").password(encoder.encode("password")).role(rolePatient).build())
                .createdBy("admin")
                .modifiedBy("admin")
                .build();
        Patient patient2 = Patient.builder()
                .dateOfBirth(LocalDate.of(1994, 1,1))
                .gender(Gender.MALE)
                .name("Aung Aung")
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .user(User.builder().username("aungaung").password(encoder.encode("password")).role(rolePatient).build())
                .createdBy("admin")
                .modifiedBy("admin")
                .build();
        Patient patient3 = Patient.builder()
                .dateOfBirth(LocalDate.of(1994, 1,1))
                .gender(Gender.MALE)
                .name("Kyaw Kyaw")
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .user(User.builder().username("kyawkyaw").password(encoder.encode("password")).role(rolePatient).build())
                .createdBy("kyawkyaw")
                .modifiedBy("kyawkyaw")
                .build();
        Patient patient4 = Patient.builder()
                .name("Phyu Phyu")
                .dateOfBirth(LocalDate.of(1993, 1,1))
                .gender(Gender.FEMALE)
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .user(User.builder().username("phyuphyu").password(encoder.encode("password")).role(rolePatient).build())
                .createdBy("phyuphyu")
                .modifiedBy("phyuphyu")
                .build();
        Patient patient5 = Patient.builder()
                .name("Su Su")
                .dateOfBirth(LocalDate.of(1991, 1,1))
                .gender(Gender.FEMALE)
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .user(User.builder().username("susu").password(encoder.encode("password")).role(rolePatient).build())
                .createdBy("susu")
                .modifiedBy("susu")
                .build();
        Patient patient6 = Patient.builder()
                .name("Mee Mee")
                .dateOfBirth(LocalDate.of(1988, 1,1))
                .gender(Gender.FEMALE)
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .user(User.builder().username("meemee").password(encoder.encode("password")).role(rolePatient).build())
                .createdBy("meemee")
                .modifiedBy("meemee")
                .build();
        Patient patient7 = Patient.builder()
                .name("Mya Mya")
                .dateOfBirth(LocalDate.of(1987, 1, 1))
                .gender(Gender.FEMALE)
                .phoneNumber("09222222222")
                .address("No.30, Bo Ta Htaung Township, Yangon")
                .user(User.builder().username("myamya").password(encoder.encode("password")).role(rolePatient).build())
                .createdBy("myamya")
                .modifiedBy("myamya")
                .build();
        patientRepository.saveAll(List.of(patient1, patient2, patient3, patient4, patient5, patient6, patient7));
        patientRepository.saveAll(List.of(patient1, patient2, patient3, patient4, patient5, patient6, patient7));

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor1)
                        .patient(patient1)
                        .timeslot("09:00 ~ 10:00")
                        .appointmentDate(LocalDate.now().plusDays(3))
                        .appointmentStatus(AppointmentStatus.PENDING)
                        .createdBy("admin")
                        .modifiedBy("admin")
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor1)
                        .patient(patient1)
                        .timeslot("09:00 ~ 10:00")
                        .appointmentDate(LocalDate.now().plusDays(3))
                        .appointmentStatus(AppointmentStatus.APPROVED)
                        .createdBy("admin")
                        .modifiedBy("admin")
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor2)
                        .patient(patient2)
                        .timeslot("10:00 ~ 11:00")
                        .appointmentDate(LocalDate.now().plusDays(5))
                        .appointmentStatus(AppointmentStatus.PENDING)
                        .createdBy("admin")
                        .modifiedBy("admin")
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor3)
                        .patient(patient3)
                        .timeslot("12:00 ~ 13:00")
                        .appointmentDate(LocalDate.now().plusDays(3))
                        .appointmentStatus(AppointmentStatus.PENDING)
                        .createdBy("kyawkyaw")
                        .modifiedBy("kyawkyaw")
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor4)
                        .patient(patient4)
                        .timeslot("15:00 ~ 16:00")
                        .appointmentDate(LocalDate.now().plusDays(6))
                        .appointmentStatus(AppointmentStatus.CANCELLED)
                        .createdBy("phyuphyu")
                        .modifiedBy("phyuphyu")
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor5)
                        .patient(patient5)
                        .timeslot("17:00 ~ 18:00")
                        .appointmentDate(LocalDate.now().plusDays(5))
                        .appointmentStatus(AppointmentStatus.PENDING)
                        .createdBy("susu")
                        .modifiedBy("susu")
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor6)
                        .patient(patient6)
                        .timeslot("19:00 ~ 20:00")
                        .appointmentDate(LocalDate.now().plusDays(4))
                        .appointmentStatus(AppointmentStatus.CANCELLED)
                        .createdBy("meemee")
                        .modifiedBy("meemee")
                        .build());

        appointmentRepository.save(
                Appointment.builder()
                        .doctor(doctor7)
                        .patient(patient7)
                        .timeslot("12:00 ~ 13:00")
                        .appointmentDate(LocalDate.now().plusDays(4))
                        .appointmentStatus(AppointmentStatus.PENDING)
                        .createdBy("myamya")
                        .modifiedBy("myamya")
                        .build());

        log.info("Data loaded");
    }
}
