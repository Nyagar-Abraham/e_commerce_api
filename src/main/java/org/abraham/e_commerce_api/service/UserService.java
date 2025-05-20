package org.abraham.e_commerce_api.service;

import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.dtos.RegisterUserRequest;
import org.abraham.e_commerce_api.dtos.UserDto;
import org.abraham.e_commerce_api.entities.Role;
import org.abraham.e_commerce_api.entities.User;
import org.abraham.e_commerce_api.exceptions.UserAlreadyExistException;
import org.abraham.e_commerce_api.mappers.UserMapper;
import org.abraham.e_commerce_api.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto registerCustomer(RegisterUserRequest request) {
        var user = getUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);
        //MAP TO DTO
        return userMapper.toDto(user);
    }

    public UserDto registerSeller(RegisterUserRequest request) {
        var user = getUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.SELLER);
        userRepository.save(user);
        //MAP TO DTO
        return userMapper.toDto(user);
    }

    private User getUser(RegisterUserRequest request) {
        //        CHECK IF USER EXISTS
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistException();
        }
        //MAP REQUEST TO ENTITY
        var user = userMapper.toEntity(request);
        return user;
    }
}
