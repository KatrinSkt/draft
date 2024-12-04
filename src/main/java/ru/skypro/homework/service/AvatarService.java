package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Avatars;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.AvatarsRepository;
import ru.skypro.homework.repository.UsersRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class AvatarService {
    private final UserContextService userContextService;
    private final AvatarsRepository avatarsRepository;

    private final Path path;

    public AvatarService(UserContextService userContextService, AvatarsRepository avatarsRepository,
                         UsersRepository usersRepository,
                         @Value("${application.avatars-dir-name}") String avatarsDirName) {
        this.userContextService = userContextService;
        this.avatarsRepository = avatarsRepository;
        path = Path.of(avatarsDirName);
    }


    public void updateUserAvatar(MultipartFile image) throws IOException {
        Users usersFromDb = userContextService.getCurrentUserFromDb();

        Avatars avatarsFromDb = avatarsRepository.findByUsersId(usersFromDb.getId());


        byte[] data = image.getBytes();
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        Path avatarsPath = path.resolve(UUID.randomUUID() + "." + extension);
        Files.write(avatarsPath, data);


        avatarsFromDb.setFilePath(avatarsPath.toString());
        avatarsFromDb.setFileSize(data.length);
        avatarsFromDb.setMediaType(image.getContentType());
        avatarsFromDb.setData(data);


        avatarsRepository.save(avatarsFromDb);
    }


}
