package org.abraham.e_commerce_api.dtos;


import lombok.Data;
import org.abraham.e_commerce_api.entities.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String mobileNo;
    private List<AddressDto> addresses = new ArrayList<>();
}
