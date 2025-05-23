package org.abraham.e_commerce_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserCredentialsRequest {

        @NotBlank(message = "firstName is required")
        @Size(max =255, message = "firstName must be less than 255 characters")
        private String firstName;

        @NotBlank(message = "lastName is required")
        @Size(max =255, message = "lastName must be less than 255 characters")
        private String lastName;

        @NotBlank(message = "mobile number is required")
        private String mobile;

        @NotBlank
        @Email(message = "provide valid email")
        private String email;

}
