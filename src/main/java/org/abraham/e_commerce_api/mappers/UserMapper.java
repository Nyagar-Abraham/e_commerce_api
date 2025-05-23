package org.abraham.e_commerce_api.mappers;


import org.abraham.e_commerce_api.dtos.RegisterUserRequest;
import org.abraham.e_commerce_api.dtos.UserDto;
import org.abraham.e_commerce_api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE , uses = {AddressMapper.class})
public interface UserMapper {
    @Mapping(target="firstName",source = "firstName")
    @Mapping(target="lastName",source = "lastName")
    @Mapping(target="email",source = "email")
    @Mapping(target="password",source = "password")
    @Mapping(target = "addresses", ignore = true)
    User toEntity(RegisterUserRequest request);
    @Mapping(target="firstName",source = "firstName")
    @Mapping(target="lastName",source = "lastName")
    @Mapping(target="email",source = "email")
    @Mapping(target="id",source = "id")
    @Mapping(target = "role",source = "role")
    @Mapping(target = "mobileNo",source = "mobileNo")
    @Mapping(target = "addresses",source = "addresses")
    UserDto toDto(User user);
}
