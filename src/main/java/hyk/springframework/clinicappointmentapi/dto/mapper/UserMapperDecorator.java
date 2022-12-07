package hyk.springframework.clinicappointmentapi.dto.mapper;

import hyk.springframework.clinicappointmentapi.domain.security.User;
import hyk.springframework.clinicappointmentapi.dto.security.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.stream.Collectors;

/**
 * @author Htoo Yanant Khin
 **/
public abstract class UserMapperDecorator implements UserMapper{
    @Autowired
//    @Qualifier("delegate")
    private RoleMapper roleMapper;

    @Autowired
    @Qualifier("delegate")
    private UserMapper userMapper;

    @Override
    public UserResponse userToUserResponse(User user) {
        UserResponse userResponse = userMapper.userToUserResponse(user);
        if (user.getRoles() != null) {
            userResponse.setRoles(user.getRoles().stream()
                    .map(role -> roleMapper.roleToRoleResponse(role))
                    .collect(Collectors.toSet()));
        }
        return userResponse;
    }
}
