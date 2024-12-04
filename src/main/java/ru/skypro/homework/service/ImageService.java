package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Avatars;
import ru.skypro.homework.model.Images;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.ImagesRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageService {
    private final Path path;
    private final ImagesRepository imagesRepository;

    public ImageService(ImagesRepository imagesRepository,
                        @Value("${application.avatars-dir-name}") String avatarsDirName) {
        this.imagesRepository = imagesRepository;
        path = Path.of(avatarsDirName);
    }

    public void saveImage(MultipartFile image, Ads ads) throws IOException {

        Images imageForDb = new Images();


        byte[] data = image.getBytes();
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        Path avatarsPath = path.resolve(UUID.randomUUID() + "." + extension);
        Files.write(avatarsPath, data);


        imageForDb.setAds(ads);
        imageForDb.setFilePath(avatarsPath.toString());
        imageForDb.setFileSize(data.length);
        imageForDb.setMediaType(image.getContentType());
        imageForDb.setData(data);


        imagesRepository.save(imageForDb);
    }

}
