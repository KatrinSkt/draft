package ru.skypro.homework.service;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.mapper.Mappers;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Images;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.AvatarsRepository;
import ru.skypro.homework.repository.ImagesRepository;
import ru.skypro.homework.repository.UsersRepository;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdService {
    private final UsersRepository usersRepository;
    private final ImagesRepository imagesRepository;
    private final Mappers mappers;
    private final AdsRepository adsRepository;
    private final AvatarsRepository avatarsRepository;

    public AdService(UsersRepository usersRepository, ImagesRepository imagesRepository, Mappers mappers, AdsRepository adsRepository, AvatarsRepository avatarsRepository) {
        this.usersRepository = usersRepository;
        this.imagesRepository = imagesRepository;
        this.mappers = mappers;
        this.adsRepository = adsRepository;
        this.avatarsRepository = avatarsRepository;
    }

    public AdsDto getAllAds() {
        List<Ads> adsList = adsRepository.findAll();
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(adsList.size());
        List<AdDto> adDtoList = new ArrayList<>();
        for (int i = 0; i < adsList.size(); i++) {
            adDtoList.add(new AdDto(
                    adsList.get(i).getUsers().getId(),
                    imagesRepository.findByAdsId(adsList.get(i).getPk()).getFilePath(), // Проверить то ли возвращается????
                    adsList.get(i).getPk(),
                    adsList.get(i).getPrice(),
                    adsList.get(i).getTitle()

            ));
        }
        adsDto.setResults(adDtoList);
        return adsDto;
    }

    public AdDto addAd(MultipartFile image, CreateOrUpdateAdDto adDto) {

        Ads ads = new Ads();

        ads = mappers.toAds(adDto);
        ads.setUsers(null); // ВОПРОС КАК ПЕРЕДЕТАТЬ ПОЛЬЗОВАТЕЛЯ?????????????

        // ПОСЛЕ КОММЕНТАРИЯ ИДЕТ КОД ЗАМЕНА mappers
        /*ads.setDescription(adDto.getDescription());
        ads.setPk(null);
        ads.setTitle(adDto.getTitle());
        ads.setUsers(); // ВОПРОС КАК ПЕРЕДЕТАТЬ ПОЛЬЗОВАТЕЛЯ?????????????
        ads.setPrice(adDto.getPrice());*/

        /////////////// // НУЖНО НАПИСАТЬ ЛОГИКУ СОХРАНЕНИЯ КАРТИНКИ В БАЗУ

        Ads adsFromDb = adsRepository.save(ads);

        AdDto adDtoFromDb = new AdDto();
        adDtoFromDb.setAuthor(adsFromDb.getUsers().getId());
        adDtoFromDb.setImage(imagesRepository.findByAdsId(adsFromDb.getPk()).getFilePath()); //Проверить то ли возвращает
        adDtoFromDb.setPk(adsFromDb.getPk());
        adDtoFromDb.setPrice(adsFromDb.getPrice());
        adDtoFromDb.setTitle(adsFromDb.getTitle());

        return adDtoFromDb;
    }

    public ExtendedAdDto getAdById(Integer id) {
        Ads adsFromDb = adsRepository.findById(id).get(); // Нужно обработать Optional
        ExtendedAdDto extendedAdDto = new ExtendedAdDto();
        extendedAdDto.setPk(adsFromDb.getPk());
        extendedAdDto.setAuthorFirstName(adsFromDb.getUsers().getFirstName());
        extendedAdDto.setAuthorLastName(adsFromDb.getUsers().getLastName());
        extendedAdDto.setDescription(adsFromDb.getDescription());
        extendedAdDto.setEmail(adsFromDb.getUsers().getEmail());
        extendedAdDto.setImage(imagesRepository.findByAdsId(adsFromDb.getPk()).getFilePath()); // Нужно проверить то ли возвращается???
        extendedAdDto.setPhone(adsFromDb.getUsers().getPhone());
        extendedAdDto.setPrice(adsFromDb.getPrice());
        extendedAdDto.setTitle(adsFromDb.getTitle());

        return extendedAdDto;
    }

    public AdDto updateAd(Integer id, CreateOrUpdateAdDto adDto) {
        Ads ads = adsRepository.findById(id).get(); // Нужно обработать Optional

        Ads adsFromDb = adsRepository.save(ads);
        AdDto adDtoFromDb = new AdDto();
        adDtoFromDb.setAuthor(adsFromDb.getUsers().getId());
        adDtoFromDb.setImage(imagesRepository.findByAdsId(adsFromDb.getPk()).getFilePath()); // Проверить то ли возвращается?
        adDtoFromDb.setPk(adsFromDb.getPk());
        adDtoFromDb.setPrice(adsFromDb.getPrice());
        adDtoFromDb.setTitle(adsFromDb.getTitle());


        return adDtoFromDb;
    }

    public void removeAd(Integer id) {
        adsRepository.deleteById(id);
    }

    public String updateAdImage(Integer id, MultipartFile image) throws IOException {
        Images images = imagesRepository.findByAdsId(id);
//!!!!!!!!! ПРОВЕРИТЬ КАК РАБОТАЕТ МЕТОД!!!!!!!!!!!!!!!
        images.setFilePath(image.getContentType());
        images.setFileSize(image.getSize());
        images.setMediaType(image.getContentType());
        images.setData(image.getBytes());
        Images imagesFromDb = imagesRepository.save(images);

        return imagesFromDb.getFilePath();
    }


    public AdsDto getMeAllAds() {

        //!!!!!!!!!!ПРОВЕРИТЬ ЧТО ВОЗВРАЩАЕТ authentication.getName() НАМ НУЖЕН ЛОГИН АВТОРИЗОВАННОГО ПОЛЬЗОВАТЕЛЯ!!!!!!!
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Users users = usersRepository.findByEmail(currentPrincipalName);
        List<Ads> ads = adsRepository.findByUsersId(users.getId());
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(ads.size());
        List<AdDto> adDtoList = new ArrayList<>();
        for (int i = 0; i < ads.size(); i++) {
            adDtoList.add(new AdDto(
                    ads.get(i).getUsers().getId(),
                    imagesRepository.findByAdsId(ads.get(i).getPk()).getFilePath(),// Проверить что возвращается!!!!!!!
                    ads.get(i).getPk(),
                    ads.get(i).getPrice(),
                    ads.get(i).getTitle()
            ));
        }


        return adsDto;
    }
}