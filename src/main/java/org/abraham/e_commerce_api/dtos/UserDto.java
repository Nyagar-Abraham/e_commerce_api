package org.abraham.e_commerce_api.dtos;


import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
