package org.abraham.e_commerce_api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDto {
    @NotBlank
    private String streetNo;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    private String buildingName;
}
