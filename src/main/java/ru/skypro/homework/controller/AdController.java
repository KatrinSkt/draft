package ru.skypro.homework.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавление объявления")
    public AdDto addAd(@RequestPart("properties") CreateOrUpdateAdDto ad,
                       @RequestPart("image") MultipartFile image) {
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
    public String updateAd(@PathVariable Integer id, @RequestBody CreateOrUpdateAdDto ad) {
        // Логика обновления объявления
        adService.updateAd(id, ad);
        return "Объявление успешно обновлено";
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объявления")
    public String removeAd(@PathVariable Integer id) {
        // Логика удаления объявления
        adService.removeAd(id);
        return "Объявление успешно удалено";
    }

    @GetMapping("/me")
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    public AdsDto getUserAds() {
        return null;
    }

    @PatchMapping("/{id}/image")
    @Operation(summary = "Обновление картинки объявления")
    public String updateUserImage(@PathVariable Integer id, @RequestBody CreateOrUpdateAdDto ad) {
        return null;
    }
}