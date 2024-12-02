package ru.skypro.homework.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdController {

    @Autowired
    private AdService adService; // Сервис для работы с объявлениями

    @GetMapping
    @Operation(summary = "Получение всех объявлений")
    public AdsDto getAllAds() {
        // Логика получения всех объявлений
        return adService.getAllAds();
    }

    @PostMapping
    @Operation(summary = "Добавление объявления")
    public AdDto addAd(@RequestParam("image") MultipartFile image, @RequestBody CreateOrUpdateAdDto ad) {
        // Логика добавления объявления
        return adService.addAd(image, ad);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение информации об объявлении")
    public ExtendedAdDto getAdById(@PathVariable Integer id) {
        // Логика получения объявления по ID
        return adService.getAdById(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление информации об объявлении")
    public AdDto updateAd(@PathVariable Integer id, @RequestBody CreateOrUpdateAdDto ad) {
        // Логика обновления объявления
        return adService.updateAd(id, ad);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объявления")
    public void removeAd(@PathVariable Integer id) {
        // Логика удаления объявления
        adService.removeAd(id);
    }

    @GetMapping("/me")
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    public AdsDto getMeAllAds() {

        return adService.getMeAllAds();
    }

    @PatchMapping("/{id}/image")
    @Operation(summary = "Обновление картинки объявления")
    public String updateAdImage(@PathVariable Integer id, @RequestParam("image") MultipartFile image) throws IOException {
        return adService.updateAdImage(id,image);
    }
}