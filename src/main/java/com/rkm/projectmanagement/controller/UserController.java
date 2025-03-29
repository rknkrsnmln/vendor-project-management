package com.rkm.projectmanagement.controller;


import com.rkm.projectmanagement.dtos.ProjectDto;
import com.rkm.projectmanagement.dtos.ResultBaseDto;
import com.rkm.projectmanagement.dtos.UserDto;
import com.rkm.projectmanagement.entities.User;
import com.rkm.projectmanagement.service.UserService;
import com.rkm.projectmanagement.system.converter.UserDtoToUserConverter;
import com.rkm.projectmanagement.system.converter.UserToUserDtoConverter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}")
public class UserController {

    private final UserService userService;

    private final UserDtoToUserConverter userDtoToUserConverter; // Convert userDto to user.

    private final UserToUserDtoConverter userToUserDtoConverter; // Convert user to userDto.


    public UserController(UserService userService, UserDtoToUserConverter userDtoToUserConverter, UserToUserDtoConverter userToUserDtoConverter) {
        this.userService = userService;
        this.userDtoToUserConverter = userDtoToUserConverter;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    @GetMapping("/users")
    public ResponseEntity<ResultBaseDto<List<UserDto>>> findAllUsers() {
        List<User> foundHogwartsUsers = this.userService.findAll();

        // Convert foundUsers to a list of UserDtos.
        List<UserDto> userDtos = foundHogwartsUsers.stream()
                .map(userDto -> this.userToUserDtoConverter.convert(userDto))
                .collect(Collectors.toList());

        // Note that UserDto does not contain password field.
        return new ResponseEntity<>(ResultBaseDto.<List<UserDto>>builder()
                .flag(true)
                .message("Finding All Success")
                .code(HttpStatus.OK.value())
                .data(userDtos)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResultBaseDto<UserDto>> findUserById(@PathVariable Integer userId) {
        User foundHogwartsUser = this.userService.findById(userId);
        UserDto userDto = this.userToUserDtoConverter.convert(foundHogwartsUser);
        return new ResponseEntity<>(ResultBaseDto.<UserDto>builder()
                .flag(true)
                .message("Finding One Success")
                .code(HttpStatus.OK.value())
                .data(userDto)
                .build(), HttpStatus.OK);
    }

    /**
     * We are not using UserDto, but User, since we require password.
     *
     * @param newUser
     * @return
     */
    @PostMapping("/users")
    public ResponseEntity<ResultBaseDto<UserDto>> addUser(@Valid @RequestBody User newUser) {
        User savedUser = this.userService.save(newUser);
        UserDto savedUserDto = this.userToUserDtoConverter.convert(savedUser);
        return new ResponseEntity<>(ResultBaseDto.<UserDto>builder()
                .flag(true)
                .message("Adding Success")
                .code(HttpStatus.OK.value())
                .data(savedUserDto)
                .build(), HttpStatus.OK);
    }

    // We are not using this to update password, need another changePassword method in this class.
    @PutMapping("/users/{userId}")
    public ResponseEntity<ResultBaseDto<UserDto>> updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto userDto) {
        User update = this.userDtoToUserConverter.convert(userDto);
        User updatedUser = this.userService.update(userId, update);
        UserDto updatedUserDto = this.userToUserDtoConverter.convert(updatedUser);
        return new ResponseEntity<>(ResultBaseDto.<UserDto>builder()
                .flag(true)
                .message("Updating Success")
                .code(HttpStatus.OK.value())
                .data(updatedUserDto)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ResultBaseDto<String>> deleteUser(@PathVariable Integer userId) {
        this.userService.delete(userId);
        return new ResponseEntity<>(ResultBaseDto.<String>builder()
                .flag(true)
                .message("Deleting Success")
                .code(HttpStatus.OK.value())
                .data("Delete user with id of " + userId + "is successful")
                .build(), HttpStatus.OK);
    }
}
