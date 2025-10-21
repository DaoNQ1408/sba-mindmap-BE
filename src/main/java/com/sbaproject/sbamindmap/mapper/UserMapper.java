package com.sbaproject.sbamindmap.mapper;

import com.sbaproject.sbamindmap.dto.request.UserRequest;
import com.sbaproject.sbamindmap.dto.response.UserResponse;
import com.sbaproject.sbamindmap.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
//    @Mapping(target = "password", ignore = true)
    UserResponse toResponse(User user);
//    User toEntity(UserRequest request);
}
