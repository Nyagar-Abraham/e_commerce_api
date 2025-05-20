package org.abraham.e_commerce_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "email is required")
    @Email
    private String email;
    @NotBlank
    @Size(min = 6,max= 10 , message = "password must be between 6 to 10 characters")
    private String password;
}
