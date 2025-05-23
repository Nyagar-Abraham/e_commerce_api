package org.abraham.e_commerce_api.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.auth.AuthUtilities;
import org.abraham.e_commerce_api.dtos.RegisterUserRequest;
import org.abraham.e_commerce_api.dtos.UpdateUserCredentialsRequest;
import org.abraham.e_commerce_api.dtos.UserDto;
import org.abraham.e_commerce_api.entities.Address;
import org.abraham.e_commerce_api.entities.Role;
import org.abraham.e_commerce_api.entities.User;
import org.abraham.e_commerce_api.exceptions.UserAlreadyExistException;
import org.abraham.e_commerce_api.mappers.UserMapper;
import org.abraham.e_commerce_api.repositories.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtilities authUtilities;

    public UserDto registerCustomer(RegisterUserRequest request) {
        var user = getUser(request);

        createUserAdresses(request, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);
        //MAP TO DTO
        return userMapper.toDto(user);
    }



    public UserDto registerSeller(RegisterUserRequest request) {
        var user = getUser(request);
        createUserAdresses(request, user);
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
        return userMapper.toEntity(request);
    }

    public UserDto getCurrentUser() {
        var user = authUtilities.getCurrentUser();
        return userMapper.toDto(user);
    }

    public List<UserDto> getAllCustomers() {
       return userRepository.findByRole(Role.CUSTOMER).stream().map(userMapper::toDto).toList();
    }

    public List<UserDto> getAllSellers() {
        return userRepository.findByRole(Role.SELLER).stream().map(userMapper::toDto).toList();
    }

    public UserDto updateCredentials(@Valid UpdateUserCredentialsRequest request, Long userId) throws BadRequestException {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new BadRequestException("Invalid userId");
        }
        user.setEmail(request.getEmail());
        user.setMobileNo(request.getMobile());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    private static void createUserAdresses(RegisterUserRequest request, User user) {
        request.getAddresses().forEach(address -> {
            var newAddress = new Address();
            newAddress.setCity(address.getCity());
            newAddress.setState(address.getState());
            newAddress.setBuildingName(address.getBuildingName());
            newAddress.setStreetNo(address.getStreetNo());
            newAddress.addUser(user);
        });
    }
}
