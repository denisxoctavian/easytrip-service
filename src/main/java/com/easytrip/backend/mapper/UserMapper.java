package com.easytrip.backend.mapper;


import com.easytrip.backend.dto.SignUpDto;
import com.easytrip.backend.dto.UserDto;
import com.easytrip.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}