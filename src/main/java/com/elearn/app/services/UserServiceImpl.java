package com.elearn.app.services;

import com.elearn.app.config.AppConstants;
import com.elearn.app.dto.UserDto;
import com.elearn.app.entities.Role;
import com.elearn.app.entities.User;
import com.elearn.app.exception.ResourceNotFoundException;
import com.elearn.app.repositories.RolesRepo;
import com.elearn.app.repositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    private UserRepo userRepo;

    private ModelMapper modelMapper;

    private RolesRepo rolesRepo;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper, RolesRepo rolesRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.rolesRepo = rolesRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto create(UserDto dto) {

        User user = modelMapper.map(dto, User.class);

        user.setUserId(UUID.randomUUID().toString());
        user.setCreateAt(new Date());
        user.setEmailVerified(false);
        user.setSmsVerified(false);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role =  rolesRepo.findByRoleName(AppConstants.ROLE_GUEST).orElseThrow(()-> new ResourceNotFoundException("Role not found !!"));
        user.assignRole(role);
        User savedUser = userRepo.save(user);
        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    public UserDto getUser(String userId) {

        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found!!!"));

        return modelMapper.map(user,UserDto.class);
    }
}
