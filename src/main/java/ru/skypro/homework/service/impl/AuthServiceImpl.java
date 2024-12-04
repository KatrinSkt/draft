package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.mapper.Mappers;
import ru.skypro.homework.model.Avatars;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.AvatarsRepository;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.AuthService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AuthServiceImpl implements AuthService {
    private final Mappers mappers;
    private final UsersRepository usersRepository;
    //  private final UserDetailsManager manager;
    private final AvatarsRepository avatarsRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(Mappers mappers, UsersRepository usersRepository, AvatarsRepository avatarsRepository, UserDetailsService userDetailsService,
                           PasswordEncoder passwordEncoder) {
        this.mappers = mappers;
        this.usersRepository = usersRepository;
        this.avatarsRepository = avatarsRepository;
        this.userDetailsService = userDetailsService;
        this.encoder = passwordEncoder;
    }

    @Override
    public boolean login(String userName, String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            return encoder.matches(password, userDetails.getPassword());
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean register(RegisterDto registerDto) throws IOException {
        if (usersRepository.findByEmail(registerDto.getUsername()).isPresent()) {
            return false;
        } else {
            Users users = mappers.toUsers(registerDto);
            users.setPassword(encoder.encode(registerDto.getPassword()));
            usersRepository.save(users);

            // Загружаем дефолтное изображение
            Avatars avatar = new Avatars();
            String defaultAvatarPath = "defaultAvatars/java.png";
            Path path = Paths.get(defaultAvatarPath);
            byte[] defaultImageData = Files.readAllBytes(path);

            avatar.setData(defaultImageData);
            avatar.setFilePath(defaultAvatarPath);
            avatar.setFileSize(defaultImageData.length);
            avatar.setMediaType("image/png");
            avatar.setUsers(users);
            avatarsRepository.save(avatar);
        }
        return true;
    }
}
