package com.elearn.app.controllers;

import com.elearn.app.dto.UserDto;
import com.elearn.app.entities.User;
import com.elearn.app.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(
            @RequestBody UserDto userDto
            ){

            return userService.create(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(
            @PathVariable String userId
    ){

       return userService.getUser(userId);
    }

}
