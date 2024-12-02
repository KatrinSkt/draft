package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.impl.AuthServiceImpl;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
//@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Tag(name = "Авторизация")
    @Operation(summary = "Авторизация пользователя")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        if (authService.login(loginDto.getUsername(), loginDto.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    @Tag(name = "Регистрация")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        if (authService.register(registerDto)) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}











    /*@PostMapping("/register")
    @Tag(name = "Регистрация")
    @Operation(summary = "Регистрация пользователя")
    public int createUser(@RequestBody RegisterDto registerDto) {



        *//*if (authService.register(registerDto)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }*//*
        return authService.createUser(registerDto);
    }*/
}
