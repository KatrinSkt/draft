package ru.skypro.homework.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.*;
import ru.skypro.homework.service.UserContextService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private UserContextService userContextService;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private AvatarsRepository avatarsRepository;

    @Autowired
    private ImagesRepository imagesRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clear() {
        commentsRepository.deleteAll();
        imagesRepository.deleteAll();
        avatarsRepository.deleteAll();
        adsRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    public void loginTest() {
        // Поля для создания Users
        String email = "aa@gmail.com";
        String firstName = "FirstName";
        String lastName = "LastName";
        String phone = "+7(000)000-00-00";
        Role roleUser = Role.USER;
        String passwordForUser = "123456789";

        // Создание и сохранение Users с полями в БД
        Users usersForSaveDb = new Users();
        usersForSaveDb.setEmail(email);
        usersForSaveDb.setFirstName(firstName);
        usersForSaveDb.setLastName(lastName);
        usersForSaveDb.setPhone(phone);
        usersForSaveDb.setRole(roleUser);
        usersForSaveDb.setPassword(passwordEncoder.encode(passwordForUser));

        // Добавляем Users в базу данных
        usersRepository.save(usersForSaveDb);

        // Проверка наличия Users в БД
        Assertions.assertEquals(usersRepository.findByEmail(email).get(), usersForSaveDb);
        //Создаем LoginDto
        LoginDto validLoginDto = new LoginDto(email, passwordForUser);

        //check
        ResponseEntity<?> response = testRestTemplate.postForEntity("/login", validLoginDto, Void.class);

        // Assert: Check that the response status is OK (200)
        assertThat(response.getStatusCode()).isEqualTo(OK);

    }

    @Test
    public void loginNegativeTest() {
        // Поля для создания Users
        String email = "aa@gmail.com";
        String firstName = "FirstName";
        String lastName = "LastName";
        String phone = "+7(000)000-00-00";
        Role roleUser = Role.USER;
        String passwordForUser = "123456789";

        // Создание и сохранение Users с полями в БД
        Users usersForSaveDb = new Users();
        usersForSaveDb.setEmail(email);
        usersForSaveDb.setFirstName(firstName);
        usersForSaveDb.setLastName(lastName);
        usersForSaveDb.setPhone(phone);
        usersForSaveDb.setRole(roleUser);
        usersForSaveDb.setPassword(passwordEncoder.encode(passwordForUser));

        // Добавляем Users в базу данных
        usersRepository.save(usersForSaveDb);

        // Проверка наличия Users в БД
        Assertions.assertEquals(usersRepository.findByEmail(email).get(), usersForSaveDb);
        //Создаем LoginDto invalidLoginDto
        String invalidPasswordForUser = "123459876765";
        LoginDto invalidLoginDto = new LoginDto(email, invalidPasswordForUser);

        // Act: Perform the login request
        ResponseEntity<?> response = testRestTemplate.postForEntity("/login", invalidLoginDto, Void.class);

        // Assert: Check that the response status is UNAUTHORIZED (401)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void registerTest() {
        List<Users> usersList = usersRepository.findAll();
        assertTrue(usersList.isEmpty());
        //Создаем RegisterDto registerDto, чтобы сохранить в БД
        String email = "aa@gmail.com";
        String firstName = "FirstName";
        String lastName = "LastName";
        String phone = "+7(000)000-00-00";
        Role roleUser = Role.USER;
        String passwordForUser = "123456789";
        //Создаем RegisterDto validRegisterDto
        RegisterDto validRegisterDto = new RegisterDto(email, passwordForUser, firstName, lastName, phone, roleUser);
        //check
        ResponseEntity<?> response = testRestTemplate.postForEntity("/register", validRegisterDto, Void.class);

        // Assert: Check that the response status is CREATED (201)
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        List<Users> usersListFromDb = usersRepository.findAll();
        Assertions.assertEquals(usersListFromDb.get(0).getFirstName(), firstName);
        Assertions.assertEquals(usersListFromDb.get(0).getLastName(), lastName);
        Assertions.assertEquals(usersListFromDb.get(0).getPhone(), phone);
        Assertions.assertEquals(usersListFromDb.get(0).getEmail(), email);


    }

    @Test
    public void registerNegativeTest() {
        List<Users> usersList = usersRepository.findAll();
        assertTrue(usersList.isEmpty());
        //Создаем RegisterDto registerDto, чтобы сохранить в БД
        String email = " ";
        String firstName = "Fi";
        String lastName = "La";
        String phone = "+7(000)000-00-00";
        Role roleUser = Role.USER;
        String passwordForUser = "123456789";
        //Создаем RegisterDto invalidRegisterDto
        RegisterDto invalidRegisterDto = new RegisterDto(email, passwordForUser, firstName, lastName, phone, roleUser);
        //check
        ResponseEntity<?> response = testRestTemplate.postForEntity("/register", invalidRegisterDto, Void.class);

        // Assert: Check that the response status is BAD_REQUEST (400)
        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }


}