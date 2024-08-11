package com.majlo.antares.mappers;

import com.majlo.antares.dtos.SignUpDto;
import com.majlo.antares.dtos.UserDto;
import com.majlo.antares.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}
