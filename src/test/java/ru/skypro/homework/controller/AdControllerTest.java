package ru.skypro.homework.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;

public class AdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AdService adService;

    @InjectMocks
    private AdController adController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adController).build();
    }

    @Test
    public void testGetAllAds() throws Exception {
        AdsDto adsDto = new AdsDto();
        when(adService.getAllAds()).thenReturn(adsDto);

        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").exists()); // Убедитесь, что поле count существует в ответе
    }

    @Test
    public void testAddAd() throws Exception {
        CreateOrUpdateAdDto ad = new CreateOrUpdateAdDto();
        ad.setTitle("Test Ad");
        ad.setPrice(100);

        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());

        when(adService.addAd(any(MultipartFile.class), any(CreateOrUpdateAdDto.class))).thenReturn(new AdDto());

        mockMvc.perform(multipart("/ads")
                        .file(image)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Ad\", \"price\":100}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAdById() throws Exception {
        ExtendedAdDto extendedAdDto = new ExtendedAdDto();
        when(adService.getAdById(1)).thenReturn(extendedAdDto);

        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateAd() throws Exception {
        CreateOrUpdateAdDto ad = new CreateOrUpdateAdDto();
        ad.setTitle("Updated Ad");

        doNothing().when(adService).updateAd(1, ad);
        when(adService.getAdById(1)).thenReturn(ad);

        mockMvc.perform(patch("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Ad\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Объявление успешно обновлено"));
    }

    @Test
    public void testRemoveAd() throws Exception {
        doNothing().when(adService).removeAd(1);

        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Объявление успешно удалено"));
    }

    @Test
    public void testGetUserAds() throws Exception {
        AdsDto adsDto = new AdsDto();
        when(adService.getUserAds()).thenReturn(adsDto);

        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateUserImage() throws Exception {
        CreateOrUpdateAdDto ad = new CreateOrUpdateAdDto();

        doNothing().when(adService).updateUserImage(1, ad);

        mockMvc.perform(patch("/ads/1/image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"image\":\"new_image_url\"}"))
                .andExpect(status().isOk());
    }
}