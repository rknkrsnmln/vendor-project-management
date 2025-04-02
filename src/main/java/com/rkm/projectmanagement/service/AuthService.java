package com.rkm.projectmanagement.service;

import com.rkm.projectmanagement.dtos.UserDto;
import com.rkm.projectmanagement.entities.MyUserPrincipal;
import com.rkm.projectmanagement.entities.User;
import com.rkm.projectmanagement.system.converter.UserToUserDtoConverter;
import com.rkm.projectmanagement.system.security.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    private final UserToUserDtoConverter userToUserDtoConverter;

    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        //Create user info here
        MyUserPrincipal authenticationPrincipal = (MyUserPrincipal) authentication.getPrincipal();
        User userAuthentication = authenticationPrincipal.getUser();
        //This would not contain any password
        UserDto userDto = this.userToUserDtoConverter.convert(userAuthentication);
        //Create JWT here
        String token = this.jwtProvider.createToken(authentication);
//        String token = "";
        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);
        return loginResultMap;
    }
}
