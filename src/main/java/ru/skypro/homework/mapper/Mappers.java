package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comments;
import ru.skypro.homework.model.Users;

@Mapper(componentModel = "spring")
public interface Mappers {

    @Mapping(target = "image", ignore = true)
    UserDto toUserDto(Users users);

    @Mapping(target = "email", source = "username")
    @Mapping(target = "id", ignore = true)
    Users toUsers(RegisterDto registerDto);

    @Mapping(source = "users.id", target = "author")
    AdDto toAdDto(Ads ads);

    @Mapping(source = "users.id", target = "author")
    @Mapping(source = "users.firstName", target = "authorFirstName")
    CommentDto toCommentDto(Comments comments);

    @Mapping(expression = "java(System.currentTimeMillis())", target = "createdAt")
    Comments toComments(CreateOrUpdateCommentDto commentDto);

    UpdateUserDto toUpdateUserDto(Users users);
}
