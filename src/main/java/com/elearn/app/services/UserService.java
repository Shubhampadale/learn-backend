package com.elearn.app.services;

import com.elearn.app.dto.UserDto;

public interface UserService {

    UserDto create(UserDto dto);

    UserDto getUser(String userId);
}
