package ru.skypro.homework.service;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdService {
    private final UserContextService userContextService;
    private final ImageService imageService;
    private final ImagesRepository imagesRepository;
    private final Mappers mappers;
    private final AdsRepository adsRepository;

    public AdService(UserContextService userContextService, ImageService imageService, UsersRepository usersRepository, ImagesRepository imagesRepository, AdsRepository adsRepository, AvatarsRepository avatarsRepository, Mappers mappers) {
        this.userContextService = userContextService;
        this.imageService = imageService;
        this.imagesRepository = imagesRepository;
        this.adsRepository = adsRepository;
        this.mappers = mappers;
    }

    public AdsDto getAllAds() {
        List<Ads> adsList = adsRepository.findAll();
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(adsList.size());
        List<AdDto> adDtoList = new ArrayList<>();
        for (int i = 0; i < adsList.size(); i++) {
            adDtoList.add(new AdDto(
                    adsList.get(i).getUsers().getId(),
                    imagesRepository.findByAdsPk(adsList.get(i).getPk()).getFilePath(), // Проверить то ли возвращается????
                    adsList.get(i).getPk(),
                    adsList.get(i).getPrice(),
                    adsList.get(i).getTitle()

            ));
        }
        adsDto.setResults(adDtoList);
        return adsDto;
    }

    public AdDto addAd(CreateOrUpdateAdDto adDto, MultipartFile image) throws IOException {
        Ads ads = new Ads();

        //* ads = mappers.toAds(adDto);
        ads.setUsers(null); // ВОПРОС КАК ПЕРЕДЕТАТЬ ПОЛЬЗОВАТЕЛЯ?????????????*//*

        // ПОСЛЕ КОММЕНТАРИЯ ИДЕТ КОД ЗАМЕНА mappers
        ads.setDescription(adDto.getDescription());
        ads.setPk(null);
        ads.setTitle(adDto.getTitle());

        Users users = userContextService.getCurrentUserFromDb();
        ads.setUsers(users);
        ads.setPrice(adDto.getPrice());


        Ads adsFromDb = adsRepository.save(ads);
        imageService.saveImage(image, ads);
        AdDto adDtoFromDb = new AdDto();
        adDtoFromDb.setAuthor(adsFromDb.getUsers().getId());
        adDtoFromDb.setImage(imagesRepository.findByAdsPk(adsFromDb.getPk()).getFilePath());
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
        extendedAdDto.setImage(imagesRepository.findByAdsPk(adsFromDb.getPk()).getFilePath()); // Нужно проверить то ли возвращается???
        extendedAdDto.setPhone(adsFromDb.getUsers().getPhone());
        extendedAdDto.setPrice(adsFromDb.getPrice());
        extendedAdDto.setTitle(adsFromDb.getTitle());

        return extendedAdDto;
    }

    public AdDto updateAd(Integer id, CreateOrUpdateAdDto adDto) {
        Ads ads = adsRepository.findById(id).get(); // Нужно обработать Optional
        ads.setTitle(adDto.getTitle());
        ads.setPrice(adDto.getPrice());
        ads.setDescription(adDto.getDescription());
        
        Ads adsFromDb = adsRepository.save(ads);

        AdDto adDtoFromDb = mappers.toAdDto(adsFromDb);
//        AdDto adDtoFromDb = new AdDto();
//        adDtoFromDb.setAuthor(adsFromDb.getUsers().getId());
        adDtoFromDb.setImage(imagesRepository.findByAdsPk(adsFromDb.getPk()).getFilePath()); // Проверить то ли возвращается?
//        adDtoFromDb.setPk(adsFromDb.getPk());
//        adDtoFromDb.setPrice(adsFromDb.getPrice());
//        adDtoFromDb.setTitle(adsFromDb.getTitle());


        return adDtoFromDb;
    }

    public void removeAd(Integer id) {
        imagesRepository.deleteById(imagesRepository.findByAdsPk(id).getId());
        adsRepository.deleteById(id);
    }

    public String updateAdImage(Integer id, MultipartFile image) throws IOException {
        Images images = imagesRepository.findByAdsPk(id);
//!!!!!!!!! ПРОВЕРИТЬ КАК РАБОТАЕТ МЕТОД!!!!!!!!!!!!!!!
        ///!!!!! ТУТ НУЖНО ПОХОЖЕ ДОПИСАТЬ УДАЛЕНИЕ КАРТИНКИ ПО ЕЕ ПУТИ И ТУДА ЖЕ ЗАПИСАТЬ НОВУЮ КАРТИНКУ !!!!!!!!


        images.setFilePath(image.getContentType());
        images.setFileSize(image.getSize());
        images.setMediaType(image.getContentType());
        images.setData(image.getBytes());
        Images imagesFromDb = imagesRepository.save(images);

        return imagesFromDb.getFilePath();
    }


    public AdsDto getMeAllAds() {
        Users users = userContextService.getCurrentUserFromDb();
        List<Ads> ads = adsRepository.findByUsersId(users.getId());

        AdsDto adsDto = new AdsDto();

        adsDto.setCount(ads.size());
        List<AdDto> adDtoList = new ArrayList<>();
        for (int i = 0; i < ads.size(); i++) {
            adDtoList.add(new AdDto(
                    ads.get(i).getUsers().getId(),
                    imagesRepository.findByAdsPk(ads.get(i).getPk()).getFilePath(),// Проверить что возвращается!!!!!!!
                    ads.get(i).getPk(),
                    ads.get(i).getPrice(),
                    ads.get(i).getTitle()
            ));
        }
        adsDto.setResults(adDtoList);
        return adsDto;
    }
}