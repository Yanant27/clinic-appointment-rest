package hyk.springframework.clinicappointmentapi.dto.security;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author Htoo Yanant Khin
 **/
@Getter
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = 7868307529966801671L;

    private final String jwttoken;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }
}
