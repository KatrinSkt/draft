
package ru.skypro.homework.service;

import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.Mappers;
import ru.skypro.homework.model.Avatars;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.AvatarsRepository;
import ru.skypro.homework.repository.UsersRepository;


@Service
public class UserService {
    private final Mappers mappers;
    private final UserContextService userContextService;
    private final AvatarsRepository avatarsRepository;
    private final PasswordEncoder encoder;
    private final UsersRepository usersRepository;

    public UserService(Mappers mappers, UserContextService userContextService, AvatarsRepository avatarsRepository, PasswordEncoder encoder, UsersRepository usersRepository) {
        this.mappers = mappers;
        this.userContextService = userContextService;
        this.avatarsRepository = avatarsRepository;
        this.encoder = encoder;
        this.usersRepository = usersRepository;
    }


    public void updatePassword(NewPasswordDto newPasswordDto) {
        Users usersFromDb = userContextService.getCurrentUserFromDb();
        usersFromDb.setPassword(encoder.encode(newPasswordDto.getNewPassword()));
        usersRepository.save(usersFromDb);
    }

    public UserDto getUser() {
        Users usersFromDb = userContextService.getCurrentUserFromDb();
        UserDto userDto =mappers.toUserDto(usersFromDb);
//        UserDto userDto = new UserDto();
//        userDto.setId(usersFromDb.getId());
//        userDto.setEmail(usersFromDb.getEmail());
//        userDto.setFirstName(usersFromDb.getFirstName());
//        userDto.setLastName(usersFromDb.getLastName());
//        userDto.setPhone(usersFromDb.getPhone());
//        userDto.setRole(usersFromDb.getRole());
        userDto.setImage(avatarsRepository.findByUsersId(usersFromDb.getId()).getFilePath());

        return userDto;
    }

    public UpdateUserDto updateUser(UpdateUserDto updateUserDto) {
        Users usersFromDb = userContextService.getCurrentUserFromDb();

        usersFromDb.setFirstName(updateUserDto.getFirstName());
        usersFromDb.setLastName(updateUserDto.getLastName());
        usersFromDb.setPhone(updateUserDto.getPhone());

        Users usersFromDbNew = usersRepository.save(usersFromDb);

//        UpdateUserDto updateUserDtoFromDb = new UpdateUserDto();
//        updateUserDtoFromDb.setFirstName(usersFromDbNew.getFirstName());
//        updateUserDtoFromDb.setLastName(usersFromDbNew.getLastName());
//        updateUserDtoFromDb.setPhone(usersFromDbNew.getPhone());
//        return updateUserDtoFromDb;

        return mappers.toUpdateUserDto(usersFromDbNew);
    }

}
