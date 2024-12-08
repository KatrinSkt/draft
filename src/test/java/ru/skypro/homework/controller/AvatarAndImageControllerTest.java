package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Images;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.*;
import ru.skypro.homework.service.UserContextService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AvatarAndImageControllerTest {

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
    public void getImageFromFsTest() throws IOException {
        // Поля для создания Users
        String email = "aa@gmail.com";
        String firstName = "FirstName";
        String lastName = "LastName";
        String phone = "+7(000)000-00-00";
        Role roleUser = Role.USER; // Предположим, что у вас есть enum Role
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
        //Создаем Ads adsForDb и сохраняем в БД
        Ads adsForDb = new Ads(1, 1000, "Title", "Description");
        //Берем юзера из БД для получения id
        Users usersFromDb = usersRepository.findByEmail(email).get();
        adsForDb.setUsers(usersFromDb);
        adsRepository.save(adsForDb);
        //Получаем объявление из БД для сравнения
        Ads adsFromDb = adsRepository.findByUsersId(usersFromDb.getId()).get(0);
        //Создаем и сохраняем в БД Images images

        Images images = new Images(1, "./defaultAvatars/", "pathForEndpoint", "image/png", adsFromDb);
        imagesRepository.save(images);
        String strFilePath = "./defaultAvatars/"+imagesRepository.findByAdsPk(adsFromDb.getPk()).getId()+".png";
        images.setFilePath(strFilePath);
        System.out.println();
        System.out.println("images " + images);
        System.out.println();
        imagesRepository.save(images);
        Files.write(Paths.get(strFilePath),"test image content".getBytes());
        //Создаем String pathForEndpoint
        String pathForEndpoint = String.valueOf(imagesRepository.findByAdsPk(adsFromDb.getPk()).getId());
        System.out.println();
        System.out.println("pathForEndpoint " + pathForEndpoint);
        System.out.println();
        // Act: Perform the GET request to fetch the image
//        ResponseEntity<byte[]> response = testRestTemplate.getForEntity("/image/{id}", byte[].class, pathForEndpoint);
//
//        // Assert: Check that the response status is OK (200)
//        assertThat(response.getStatusCode()).isEqualTo(OK);
//        Files.delete(Paths.get(strFilePath));
        // Optionally, assert that the body is not null and contains expected data
//        byte[] imageData = response.getBody();
//        assertThat(imageData).isNotNull();
//        assertThat(imageData.length).isGreaterThan(0); // Check that the image data is not empty
//        assertThat(imageData.length).isEqualTo("test image content".getBytes());
//        Files.delete(Paths.get(strFilePath));
    }


//    @GetMapping("/image/{id}")
//    @Operation(summary = "Загрузка картинки")
//    public ResponseEntity<byte[]> getImageFromFs(@PathVariable("id") String pathForEndpoint) throws IOException {
//        logger.info("Fetching image with ID: {}", pathForEndpoint);
//        Pair<byte[], String> imageData = imageService.getImageFromFs(pathForEndpoint);
//        logger.info("Successfully fetched image with ID: {}", pathForEndpoint);
//        return buildResponseEntity(imageData);
//    }


}