package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Users;
@Mapper(componentModel = "spring")
public interface Mappers {
    @Mapping(target = "image", ignore = true)
    UserDto toUserDto(Users users);
    @Mapping(target = "email", source = "username")
    @Mapping(target = "id", ignore = true)
        //@Mapping(target = "image", ignore = true)
    Users toUsers(RegisterDto registerDto);
    @Mapping(target = "pk", ignore = true)
    Ads toAds (CreateOrUpdateAdDto createOrUpdateAdDto);
}
