
package ru.skypro.homework.service;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.UsersRepository;


import java.util.HashMap;


@Service
public class UserService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    /*private Map<String, User> userDatabase = new HashMap<>(); // Хранение пользователей в памяти
    private String currentUserId = "user1"; // Пример текущего пользователя*/

    /*public boolean updatePassword(String currentPassword, String newPassword) {
        User currentUser = getCurrentUser();
        // Проверка текущего пароля (в реальном приложении следует использовать хэширование паролей)
        if (currentUser.getPassword().equals(currentPassword)) {
            currentUser.setPassword(newPassword);
            return true;
        }
        return false; // Возврат false, если текущий пароль неверен
    }*/

    public UserDto getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println("getUser "+currentPrincipalName);
        Users usersFromDb = usersRepository.findByEmail(currentPrincipalName);
        UserDto userDto = new UserDto();
        userDto.setId(usersFromDb.getId());
        userDto.setEmail(usersFromDb.getEmail());
        userDto.setFirstName(usersFromDb.getFirstName());
        userDto.setLastName(usersFromDb.getLastName());
        userDto.setPhone(usersFromDb.getPhone());
        userDto.setRole(usersFromDb.getRole());
        userDto.setImage(null); //!!!!!!!!!!!!!!!!!1ДОПИСАТЬ КАРТИНКУ!!!!!!!!!!!!!!
        return userDto;
    }

    /*public void updateUser(UpdateUser updateUserDto) {
        User currentUser = getCurrentUser();
        // Обновление информации о пользователе
        if (updateUserDto.getName() != null) {
            currentUser.setName(updateUserDto.getName());
        }
        if (updateUserDto.getEmail() != null) {
            currentUser.setEmail(updateUserDto.getEmail());
        }
        // Дополнительные поля могут быть добавлены здесь
    }*/

    /*public void updateUserImage(MultipartFile image) {
        User currentUser = getCurrentUser();
        // Логика сохранения изображения (например, сохранение на диск или в облачное хранилище)
        // Здесь можно добавить код для обработки файла и обновления аватара пользователя
        currentUser.setImageUrl("url_to_updated_image"); // Пример обновления URL изображения
    }*/
}
