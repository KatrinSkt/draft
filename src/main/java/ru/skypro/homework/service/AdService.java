package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.model.User;

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

    public AdDto addAd(MultipartFile image, CreateOrUpdateAdDto adDto, int id) {
        User user = null; //userRepository.findById(id.toString);



        //
        return null;//adDtoRepository.save(adDto1);
    }

    public ExtendedAdDto getAdById(Integer id) {
        Optional<AdDto> adOptional = adDtoList.stream().filter(ad -> ad.getPk().equals(id)).findFirst();
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

    public Object getUserAds() {
        return null;
    }

    public void updateUserImage(int i, CreateOrUpdateAdDto ad) {

    }
}