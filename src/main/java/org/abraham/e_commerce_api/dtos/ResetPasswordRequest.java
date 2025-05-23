package org.abraham.e_commerce_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "password is required")
    @Size(min = 6, max = 10 ,message = "password must be 6 to 10 characters long")
    private String newPassword;
}
