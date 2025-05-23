package org.abraham.e_commerce_api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.dtos.RegisterUserRequest;
import org.abraham.e_commerce_api.dtos.UpdateUserCredentialsRequest;
import org.abraham.e_commerce_api.dtos.UserDto;
import org.abraham.e_commerce_api.exceptions.UserAlreadyExistException;
import org.abraham.e_commerce_api.mappers.UserMapper;
import org.abraham.e_commerce_api.repositories.UserRepository;
import org.abraham.e_commerce_api.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserService userService;

    //    REGISTER USER
    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody RegisterUserRequest request, UriComponentsBuilder uriBuilder) {
        var userDto = userService.registerCustomer(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

//    REGISTER SELLER
    @PostMapping("/register/seller")
    public ResponseEntity<?> registerSeller(@Valid @RequestBody RegisterUserRequest request, UriComponentsBuilder uriBuilder) {
        var userDto = userService.registerSeller(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

//    GET CURRENT USER
    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var userDto = userService.getCurrentUser();
        return ResponseEntity.ok(userDto);
    }

//    GET ALL CUSTOMERS
    @GetMapping("/customers")
    public ResponseEntity<List<UserDto>> getCustomers() {
        var userDtoList = userService.getAllCustomers();
        return ResponseEntity.ok(userDtoList);
    }

    //    GET ALL SELLERS
    @GetMapping("/sellers")
    public ResponseEntity<List<UserDto>> getSellers() {
        var userDtoList = userService.getAllSellers();
        return ResponseEntity.ok(userDtoList);
    }

//    UPDATE CREDENTIALS
    @PutMapping("/update/credentials/{user_id}")
    public ResponseEntity<?> updateCredentials(@Valid @RequestBody UpdateUserCredentialsRequest request , @PathVariable("user_id") Long userId) throws BadRequestException {
        var userDto = userService.updateCredentials(request,userId);
        return ResponseEntity.ok(userDto);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<?> UserAlreadyExistException(UserAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("error",e.getMessage()));

    }
}
