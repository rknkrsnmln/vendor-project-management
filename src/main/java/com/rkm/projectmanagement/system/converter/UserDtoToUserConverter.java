package com.rkm.projectmanagement.system.converter;

import com.rkm.projectmanagement.dtos.UserDto;
import com.rkm.projectmanagement.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {

    @Override
    public User convert(UserDto source) {
        User userUser = new User();
        userUser.setUsername(source.username());
        userUser.setEnabled(source.enabled());
        userUser.setRoles(source.roles());
        return userUser;
    }

}
