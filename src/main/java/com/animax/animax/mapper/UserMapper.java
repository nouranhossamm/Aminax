package com.animax.animax.mapper;

import com.animax.animax.dto.requests.SignupRequest;
import com.animax.animax.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User signUpRequestToEntity(SignupRequest signUpRequest);
    SignupRequest toDto(User user);
}