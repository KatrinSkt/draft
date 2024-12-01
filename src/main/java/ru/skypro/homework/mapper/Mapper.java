package ru.skypro.homework.mapper;

import org.mapstruct.Mapping;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Users;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {
    @Mapping(target = "image", ignore = true)
    UserDto toUserDto(Users users);
    @Mapping(target = "email", source = "username")
    @Mapping(target = "id", ignore = true)
    //@Mapping(target = "image", ignore = true)
    Users toUsers(RegisterDto registerDto);


}
