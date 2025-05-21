package org.abraham.e_commerce_api.mappers;


import org.abraham.e_commerce_api.dtos.RegisterUserRequest;
import org.abraham.e_commerce_api.dtos.UserDto;
import org.abraham.e_commerce_api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(RegisterUserRequest request);
    UserDto toDto(User user);
}
