//package ru.skypro.homework.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.util.Pair;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//import ru.skypro.homework.model.Avatars;
//import ru.skypro.homework.model.Users;
//import ru.skypro.homework.repository.AvatarsRepository;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//@Service
//public class AvatarService {
//    private final UserContextService userContextService;
//    private final AvatarsRepository avatarsRepository;
//    private final Path path;
//
//    public AvatarService(UserContextService userContextService,
//                         AvatarsRepository avatarsRepository,
//                         @Value("${application.avatars-dir-name}") String avatarsDirName) {
//        this.userContextService = userContextService;
//        this.avatarsRepository = avatarsRepository;
//        path = Path.of(avatarsDirName);
//    }
//
//    public void saveDefaultAvatar(Users users) throws IOException {
//        Avatars avatar = new Avatars();
//        String defaultAvatarPath = "defaultAvatars/java.png";
//        Path pathDefaultAvatar = Paths.get(defaultAvatarPath);
//        byte[] data = Files.readAllBytes(pathDefaultAvatar);
//        String fileName = pathDefaultAvatar.getFileName().toString();
//        String extension = getFileExtension(fileName);
//        Path avatarsPathNew = path.resolve(UUID.randomUUID() + "." + extension);
//        Files.write(avatarsPathNew, data);
//        avatar.setFilePath(avatarsPathNew.toString());
//        String setPathForEndpointNew = "/image/avatar/" + users.getId();
//        avatar.setPathForEndpoint(setPathForEndpointNew);
//        avatar.setMediaType("image/png");
//        avatar.setUsers(users);
//        avatarsRepository.save(avatar);
//    }
//
//    private String getFileExtension(String fileName) {
//        int dotIndex = fileName.lastIndexOf('.');
//        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
//            return ""; // Нет расширения
//        }
//        return fileName.substring(dotIndex + 1);
//    }
//
//    public void updateUserAvatar(MultipartFile image) throws IOException {
//        Avatars avatarsFromDb = avatarsRepository.findByUsersId(userContextService.getCurrentUserFromDb().getId());
//        byte[] data = image.getBytes();
//        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
//        Path avatarsPathOld = Paths.get(avatarsFromDb.getFilePath());
//        Files.delete(avatarsPathOld);
//        Path avatarsPath = path.resolve(UUID.randomUUID() + "." + extension);
//        Files.write(avatarsPath, data);
//        avatarsFromDb.setFilePath(avatarsPath.toString());
//        avatarsFromDb.setMediaType(image.getContentType());
//        avatarsRepository.save(avatarsFromDb);
//    }
//    public Pair<byte[], String> getAvatarFromFs(String pathForEndpoint) throws IOException {
//        Avatars avatar = avatarsRepository.findByUsersId(Integer.parseInt(pathForEndpoint));
//        return Pair.of(Files.readAllBytes(Paths.get(avatar.getFilePath())), avatar.getMediaType());
//    }
//
//}
package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.AvatarNotFoundException; // Предполагается, что это исключение существует
import ru.skypro.homework.model.Avatars;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.AvatarsRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Сервис для управления аватарами пользователей.
 * <p>
 * Этот класс предоставляет методы для сохранения, обновления, получения и удаления аватаров пользователей.
 * Он также отвечает за загрузку аватаров по умолчанию и взаимодействие с файловой системой для хранения изображений.
 * </p>
 */
@Service
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final UserContextService userContextService;
    private final AvatarsRepository avatarsRepository;
    private final Path path;

    public AvatarService(UserContextService userContextService,
                         AvatarsRepository avatarsRepository,
                         @Value("${application.avatars-dir-name}") String avatarsDirName) {
        this.userContextService = userContextService;
        this.avatarsRepository = avatarsRepository;
        this.path = Path.of(avatarsDirName);
    }

    /**
     * Сохраняет аватар по умолчанию для указанного пользователя.
     *
     * @param users объект Users, для которого сохраняется аватар.
     * @throws IOException если возникает ошибка при чтении или записи файла.
     */
    public void saveDefaultAvatar(Users users) throws IOException {
        String defaultAvatarPath = "defaultAvatars/java.png";
        Path pathDefaultAvatar = Paths.get(defaultAvatarPath);

        byte[] data = Files.readAllBytes(pathDefaultAvatar);
        String extension = getFileExtension(pathDefaultAvatar.getFileName().toString());
        Path avatarsPathNew = path.resolve(UUID.randomUUID() + "." + extension);

        Files.write(avatarsPathNew, data);

        Avatars avatar = new Avatars();
        avatar.setFilePath(avatarsPathNew.toString());
        avatar.setPathForEndpoint("/image/avatar/" + users.getId());
        avatar.setMediaType("image/png");
        avatar.setUsers(users);

        avatarsRepository.save(avatar);

        logger.info("Default avatar saved for user ID: {}", users.getId());
    }

    /**
     * Обновляет аватар пользователя.
     *
     * @param image объект MultipartFile, представляющий новое изображение.
     * @throws IOException если возникает ошибка при чтении или записи файла.
     * @throws AvatarNotFoundException если аватар не найден для текущего пользователя.
     */
    public void updateUserAvatar(MultipartFile image) throws IOException {
        Users currentUser = userContextService.getCurrentUserFromDb();

        Avatars avatarsFromDb = avatarsRepository.findByUsersId(currentUser.getId())
                .orElseThrow(() -> new AvatarNotFoundException("Avatar not found for user ID: " + currentUser.getId()));

        // Удаляем старый аватар
        deleteExistingAvatar(avatarsFromDb);

        // Сохраняем новый аватар
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        Path avatarsPath = path.resolve(UUID.randomUUID() + "." + extension);
        Files.write(avatarsPath, image.getBytes());

        avatarsFromDb.setFilePath(avatarsPath.toString());
        avatarsFromDb.setMediaType(image.getContentType());

        avatarsRepository.save(avatarsFromDb);

        logger.info("Updated avatar for user ID: {}", currentUser.getId());
    }

    /**
     * Получает аватар пользователя из файловой системы.
     *
     * @param pathForEndpoint путь для доступа к аватару.
     * @return пара, содержащая байты аватара и его тип медиа.
     * @throws IOException если возникает ошибка при чтении файла.
     * @throws AvatarNotFoundException если аватар не найден по указанному идентификатору пользователя.
     */
    public Pair<byte[], String> getAvatarFromFs(String pathForEndpoint) throws IOException {
        Integer userId = Integer.parseInt(pathForEndpoint);

        Avatars avatar = avatarsRepository.findByUsersId(userId)
                .orElseThrow(() -> new AvatarNotFoundException("Avatar not found for user ID: " + userId));

        byte[] avatarData = Files.readAllBytes(Paths.get(avatar.getFilePath()));

        logger.info("Retrieved avatar data for user ID: {}", userId);

        return Pair.of(avatarData, avatar.getMediaType());
    }

    /**
     * Удаляет существующий аватар из файловой системы.
     *
     * @param existingAvatar объект Avatars, представляющий текущий аватар.
     * @throws IOException если возникает ошибка при удалении файла.
     */
    private void deleteExistingAvatar(Avatars existingAvatar) throws IOException {
        Path existingPath = Paths.get(existingAvatar.getFilePath());

        if (Files.exists(existingPath)) {
            Files.delete(existingPath);
            logger.info("Deleted existing avatar file at path: {}", existingPath);
        } else {
            logger.warn("Attempted to delete non-existing avatar file at path: {}", existingPath);
        }
    }

    /**
     * Получает расширение файла из имени файла.
     *
     * @param fileName имя файла.
     * @return расширение файла или пустую строку, если расширение отсутствует.
     */
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return ""; // Нет расширения
        }

        return fileName.substring(dotIndex + 1);
    }
}
