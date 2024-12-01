package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;


@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    public String setPassword(@RequestBody NewPasswordDto newPasswordDto) {
        // Логика обновления пароля
//        boolean isUpdated = userService.updatePassword(newPassword.getCurrentPassword(), newPassword.getNewPassword());
//        if (isUpdated) {
//            return "Пароль успешно обновлён";
//        } else {
//            throw new RuntimeException("Ошибка обновления пароля");
//        }
        return null;
    }

    @GetMapping("/me")
    @Operation(summary = "Получение информации об авторизованном пользователе")
    public UserDto getUser() {
        System.out.println("@GetMapping(\"/me\")");
UserDto userDto = userService.getUser();
        // Логика получения информации о текущем пользователе
//
        return userDto;
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    public String updateUser(@RequestBody UpdateUserDto updateUserDto) {
        // Логика обновления информации о пользователе
//        userService.updateUser(updateUser);
//        return "Информация о пользователе обновлена";
        return null;
    }

    @PatchMapping("/me/image")
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    public String updateUserImage(@RequestParam("image") MultipartFile image) {
        // Логика обновления аватара пользователя
//        userService.updateUserImage(image);
//        return "Аватар успешно обновлён";
        return null;
    }
}
