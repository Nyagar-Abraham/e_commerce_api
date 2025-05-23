package org.abraham.e_commerce_api.mappers;

import org.abraham.e_commerce_api.dtos.AddressDto;
import org.abraham.e_commerce_api.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    @Mapping(target = "streetNo",source = "streetNo")
    @Mapping(target = "state",source = "state")
    @Mapping(target = "city",source = "city")
    @Mapping(target = "buildingName",source = "buildingName")
   AddressDto toDto(Address address);
}
