package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.dto.security.UserResponse;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

/**
 * @author Htoo Yanant Khin
 **/
@Mapper
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {
    UserResponse userToUserResponse(User user);
}
