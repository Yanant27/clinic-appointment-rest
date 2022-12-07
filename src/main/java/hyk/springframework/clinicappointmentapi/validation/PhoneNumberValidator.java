package hyk.springframework.clinicappointmentapi.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    /*
     +959123456789
     959425272093
     09123456789
     */
    private static final String PHONE_NUMBER_PATTERN = "(\\+)?(95)?((9\\d{9})|(09)(\\d{9}))";
    private static final Pattern PATTERN = Pattern.compile(PHONE_NUMBER_PATTERN);

    @Override
    public boolean isValid(final String phoneNumber, final ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        } else {
            return (validatePhoneNumber(phoneNumber));
        }
    }

    private boolean validatePhoneNumber(final String phoneNumber) {
        Matcher matcher = PATTERN.matcher(phoneNumber);
        return matcher.matches();
    }
}
