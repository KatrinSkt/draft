package ru.skypro.homework.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Avatars;
import ru.skypro.homework.model.Comments;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.*;
import ru.skypro.homework.service.UserVerification;

import javax.persistence.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.skypro.homework.dto.Role.USER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@WebMvcTest(CommentController.class)
//@Import(WebSecurityConfig.class)


public class CommentControllerTest {
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
    //    @Autowired
//    private ;
    @Autowired
    private ImagesRepository imagesRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @LocalServerPort
    private int port;
    @AfterEach
    void clear() {
        commentsRepository.deleteAll();
        imagesRepository.deleteAll();
        avatarsRepository.deleteAll();
        adsRepository.deleteAll();
        usersRepository.deleteAll();
    }

    // @WithMockUser(username = "testuser", roles = {"USER"})
    @Test
    public void addCommentTest() {
//        String auth = "username:password";
//        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
//        String authHeader = "Basic " + new String(encodedAuth);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", authHeader);
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getInterceptors().add((request, body, execution) -> {
//            request.getHeaders().set("Authorization", authHeader);
//            return execution.execute(request, body);
//        });


        //Поля для создания Users
        Integer idUser = null;
        String email = "aa@gmail.com";
        String firstName = "FirstName";
        String lastName = "LastName";
        String phone = "+7(000)000-00-00";
        Role roleUser = USER;
        String passwordForUser = "123456789";

        //Создание и сохранение Users с полями в БД
        Users usersForSaveDb = new Users();
        usersForSaveDb.setId(idUser);
        usersForSaveDb.setEmail(email);
        usersForSaveDb.setFirstName(firstName);
        usersForSaveDb.setLastName(lastName);
        usersForSaveDb.setPhone(phone);
        usersForSaveDb.setRole(roleUser);
        usersForSaveDb.setPassword(passwordEncoder.encode(passwordForUser));

        //Добавляем Users в базу данных
        usersRepository.save(usersForSaveDb);

        //Проверка наличия Users в БД
        Assertions.assertEquals(usersRepository.findByEmail(email).get(), usersForSaveDb);

        //Получаем Users из БД для получения id
        Users usersFromDB = usersRepository.findByEmail(email).get();

        //Поля для создания Ads
        Integer pkAds = null;
        Integer price = 1000;
        String title = "Title";
        String description = "Description";

        //Создание и сохранение Ads с полями в БД
        Ads adsForSaveDb = new Ads();
        adsForSaveDb.setPk(pkAds);
        adsForSaveDb.setPrice(price);
        adsForSaveDb.setTitle(title);
        adsForSaveDb.setDescription(description);
        adsForSaveDb.setUsers(usersFromDB);
        adsRepository.save(adsForSaveDb);

        System.out.println();
        System.out.println("adsForSaveDb " +adsForSaveDb);
        System.out.println();

        //Создаем CreateOrUpdateCommentDto
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto();
        String textForCreateOrUpdateCommentDto = "CreateOrUpdateCommentDto";
        createOrUpdateCommentDto.setText(textForCreateOrUpdateCommentDto);

        //Получаем из базы объявление List<Ads> чтобы получить его pk
        List<Ads> adsList = adsRepository.findByUsersId(usersFromDB.getId());
        Integer idAds = adsList.get(0).getPk();

        //Создаем комментарий для сравнения с тем который добавился в базу через контроллер
        Integer pk = null;
        String text = "TestComments";
        Long createdAt = 10000L;
        Comments commentsExpected = new Comments();
        commentsExpected.setPk(pk);
        commentsExpected.setText(text);
        commentsExpected.setCreatedAt(createdAt);
        commentsExpected.setUsers(usersFromDB);
        commentsExpected.setAds(adsList.get(0));
        System.out.println();
        System.out.println("adsList.get(0) " + adsList.get(0));
        System.out.println();

        //Создаем аватарку для пользователя которого создали
        Avatars avatarsForUser = new Avatars();
        avatarsForUser.setId(null);
        avatarsForUser.setFilePath("defaultAvatars/java.png");
        avatarsForUser.setPathForEndpoint("/image/avatar/" + usersFromDB.getId());
        avatarsForUser.setMediaType("image/png");
        avatarsForUser.setUsers(usersFromDB);


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer your_access_token_here"); // Add authentication if required

        // Create an entity with body and headers
        HttpEntity<CreateOrUpdateCommentDto> entity = new HttpEntity<>(createOrUpdateCommentDto, headers);

        // Send POST request
        ResponseEntity<CommentDto> responseEntity = testRestTemplate.exchange(
                "/ads/" + idAds + "/comments",
                HttpMethod.POST,
                entity,
                CommentDto.class);




//        ResponseEntity<CommentDto> forEntity = testRestTemplate
//                .postForEntity("http://localhost:"+port+"/ads/" + idAds + "/comments", createOrUpdateCommentDto, CommentDto.class);
//        //check
//        CommentDto actual = forEntity.getBody();
//        assertThat(actual).isNotNull();
//        assertThat(actual).usingRecursiveComparison()
//                .ignoringFields("pk")
//                .ignoringFields("createdAt")
//                .isEqualTo(commentsExpected);


//        @Test
//        @DisplayName("Создание факультета")
//        public void createFacultyTest() {
//            //data
//            Faculty faculty = createFaculty();
//            //test
//            ResponseEntity<Faculty> forEntity = testRestTemplate
//                    .postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
//            //check
//            Assertions.assertEquals(forEntity.getStatusCode(), HttpStatusCode.valueOf(200));
//            Faculty created = forEntity.getBody();
//            assertThat(created).isNotNull();
//            assertThat(created).usingRecursiveComparison()
//                    .ignoringFields("id")
//                    .isEqualTo(faculty);
//            Optional<Faculty> fromDb = facultyRepository.findById(created.getId());
//            assertThat(fromDb).isPresent();
//            assertThat(fromDb.get())
//                    .usingRecursiveComparison()
//                    .ignoringFields("id")
//                    .isEqualTo(faculty);
//
//        }


    }
//    @Test
//    @WithMockUser(username = "testuser", roles = {"USER"})
//    public void deleteCommentTest(){
//    public void deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
//
//        commentService.deleteComment(adId, commentId);

//}
//    @Test
//    @WithMockUser(username = "testuser", roles = {"USER"})
//    public void testGetComments() throws Exception {

//        @WithMockUser
//        void getCustomer() throws Exception {
//            when(customerRepository.findById(1L))
//                    .thenReturn(Optional.of(new Customer(1L, "John", "Doe")));
//
//            mockMvc.perform(get("/customer/{id}", 1L))
//                    .andExpect(status().isOk());
//        }
// Подготовка данных
//        Integer adId = 1;
//        CommentsDto commentsDto = new CommentsDto();
//        // Здесь можно добавить комментарии в commentsDto для проверки
//
//        when(commentService.getCommentsForAd(adId)).thenReturn(commentsDto);
//
//        // Выполнение запроса и проверка результата
//        mockMvc.perform(get("/" + adId + "/comments"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.comments").exists()); // Предполагая, что в CommentsDto есть поле comments
//
//        verify(commentService).getCommentsForAd(adId);
    //   }

//    @Test
//    public void testAddComment() throws Exception {
//        // Подготовка данных
//        Integer adId = 1;
//        CreateOrUpdateCommentDto newComment = new CreateOrUpdateCommentDto();
//        CommentDto addedComment = new CommentDto();
//        addedComment.setPk(1); // Установка идентификатора добавленного комментария
//
//        when(commentService.addComment(eq(adId), any(CreateOrUpdateCommentDto.class))).thenReturn(addedComment);
//
//        // Выполнение запроса и проверка результата
//        mockMvc.perform(post("/" + adId + "/comments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"text\": \"Новый комментарий\"}")) // Пример JSON для комментария
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.pk").value(1));
//
//        verify(commentService).addComment(eq(adId), any(CreateOrUpdateCommentDto.class));
//    }
//
//    @Test
//    public void testUpdateComment() throws Exception {
//        // Подготовка данных
//        Integer adId = 1;
//        Integer commentId = 1;
//        CreateOrUpdateCommentDto updatedComment = new CreateOrUpdateCommentDto();
//        CommentDto returnedComment = new CommentDto();
//
//        when(commentService.updateComment(eq(adId), eq(commentId), any(CreateOrUpdateCommentDto.class)))
//                .thenReturn(returnedComment);
//
//        // Выполнение запроса и проверка результата
//        mockMvc.perform(patch("/" + adId + "/comments/" + commentId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"text\": \"Обновленный комментарий\"}")) // Пример JSON для обновления комментария
//                .andExpect(status().isOk());
//
//        verify(commentService).updateComment(eq(adId), eq(commentId), any(CreateOrUpdateCommentDto.class));
//    }
//
//    @Test
//    public void testDeleteComment() throws Exception {
//        // Подготовка данных
//        Integer adId = 1;
//        Integer commentId = 1;
//
//        // Выполнение запроса и проверка результата
//        mockMvc.perform(delete("/" + adId + "/comments/" + commentId))
//                .andExpect(status().isOk());
//
//        verify(commentService).deleteComment(eq(adId), eq(commentId));
//    }


}