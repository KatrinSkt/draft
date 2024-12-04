package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.AvatarService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {

    private final AvatarService avatarService;
    private final UserService userService;

    public UserController(AvatarService avatarService, UserService userService) {
        this.avatarService = avatarService;
        this.userService = userService;
    }

    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    public void updatePassword(@RequestBody NewPasswordDto newPasswordDto) {
        userService.updatePassword(newPasswordDto);
    }

    @GetMapping("/me")
    @Operation(summary = "Получение информации об авторизованном пользователе")
    public UserDto getUser() {
        return userService.getUser();
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    public UpdateUserDto updateUser(@RequestBody UpdateUserDto updateUserDto) {
        return userService.updateUser(updateUserDto);
    }

    @PatchMapping("/me/image")
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    public void updateUserAvatar(@RequestParam("image") MultipartFile image) throws IOException {
        avatarService.updateUserAvatar(image);
    }

}
