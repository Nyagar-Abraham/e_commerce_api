package org.abraham.e_commerce_api.mappers;

import org.abraham.e_commerce_api.dtos.RegisterUserRequest;
import org.abraham.e_commerce_api.dtos.UserDto;
import org.abraham.e_commerce_api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

//    @Mapping(target = "firstName", source = "firstName")
//    @Mapping(target = "lastName", source = "lastName")
//    @Mapping(target = "email", source = "email")
//    @Mapping(target = "password", source = "password")
    User toEntity(RegisterUserRequest request);

    UserDto toDto(User user);
}
