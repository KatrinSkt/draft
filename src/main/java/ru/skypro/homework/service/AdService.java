package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdService {

    private List<AdDto> adDtoList = new ArrayList<>(); // Хранение объявлений в памяти (можно заменить на БД)
    private int currentId = 1; // Счетчик ID для новых объявлений

    public AdsDto getAllAds() {
        AdsDto adsDto = new AdsDto();
        adsDto.setResults(adDtoList);
        return adsDto;
    }

    public AdDto addAd(MultipartFile image, CreateOrUpdateAdDto adDto) {
        // Логика обработки изображения (например, сохранение на диск) может быть добавлена здесь

        AdDto newAdDto = new AdDto();
        newAdDto.setPk(currentId++);
        newAdDto.setTitle(adDto.getTitle());
        newAdDto.setPrice(adDto.getPrice());

        // Сохранение нового объявления в списке
        adDtoList.add(newAdDto);
        return newAdDto;
    }

    public ExtendedAdDto getAdById(Integer id) {
        Optional<AdDto> adOptional = adDtoList.stream().filter(adDto -> adDto.getPk().equals(id)).findFirst();
        if (adOptional.isPresent()) {
            AdDto adDto = adOptional.get();
            ExtendedAdDto extendedAdDto = new ExtendedAdDto();
            extendedAdDto.setPk(adDto.getPk());
            extendedAdDto.setTitle(adDto.getTitle());
            extendedAdDto.setPrice(adDto.getPrice());
            // Дополнительные поля могут быть добавлены здесь
            return extendedAdDto;
        }
        throw new RuntimeException("Объявление не найдено");
    }

    public void updateAd(Integer id, CreateOrUpdateAdDto adDto) {
//        Ad existingAd = repository.getAdById(id);
//        existingAd.setTitle(adDto.getTitle());
//        existingAd.setPrice(adDto.getPrice());
    }

    public void removeAd(Integer id) {
//        Ad adToRemove = repository.getAdById(id);
//        adList.remove(adToRemove);
    }
}