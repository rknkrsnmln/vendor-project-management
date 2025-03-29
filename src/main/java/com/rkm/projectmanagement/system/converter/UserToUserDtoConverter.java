package com.rkm.projectmanagement.system.converter;

import com.rkm.projectmanagement.dtos.UserDto;
import com.rkm.projectmanagement.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User source) {
        // We are not setting password in DTO.
        final UserDto userDto = new UserDto(source.getId(),
                source.getUsername(),
                source.isEnabled(),
                source.getRoles());
        return userDto;
    }

}