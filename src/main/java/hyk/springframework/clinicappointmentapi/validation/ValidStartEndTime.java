package hyk.springframework.clinicappointmentapi.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Htoo Yanant Khin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartEndTimeValidator.class)
public @interface ValidStartEndTime {

    String message() default "Start time must be less than end time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}