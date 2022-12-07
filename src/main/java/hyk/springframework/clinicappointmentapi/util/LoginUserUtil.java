package hyk.springframework.clinicappointmentapi.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Htoo Yanant Khin
 **/
@Slf4j
public class LoginUserUtil {
    public static String getLoginUsername() {
        log.debug("Get currently login username");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
            authentication.getPrincipal() != null &&
            authentication instanceof UsernamePasswordAuthenticationToken) {
            return authentication.getName();
        } else {
            return "";
        }
    }
}
