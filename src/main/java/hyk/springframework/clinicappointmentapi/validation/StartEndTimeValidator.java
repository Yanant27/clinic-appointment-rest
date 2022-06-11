package hyk.springframework.clinicappointmentapi.validation;

import hyk.springframework.clinicappointmentapi.web.dto.ScheduleDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartEndTimeValidator implements ConstraintValidator<ValidStartEndTime, ScheduleDTO> {
    @Override
    public boolean isValid(final ScheduleDTO scheduleDTO, final ConstraintValidatorContext context) {
        return scheduleDTO.getStartTime().isBefore(scheduleDTO.getEndTime());
    }
}