package org.abraham.e_commerce_api.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "firstName is required")
    @Size(max =255, message = "firstName must be less than 255 characters")
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Size(max =255, message = "lastName must be less than 255 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 10,message = "Password must be between 6 to 25 characters")
    private String password;
}
