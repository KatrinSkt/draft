package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
    private final UsersRepository usersRepository;
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UsersRepository usersRepository, UserDetailsManager manager,
                           PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.manager = manager;
        this.encoder = passwordEncoder;
    }

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(RegisterDto registerDto) {
        if (manager.userExists(registerDto.getUsername())) {
            return false;
        }
        manager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(registerDto.getPassword())
                        .username(registerDto.getUsername())
                        .roles(registerDto.getRole().name())
                        .build());
        return true;
    }

    @Override
    public int createUser(RegisterDto registerDto) {
        Users users = new Users();
        users.setEmail(registerDto.getUsername());
        users.setFirstName(registerDto.getFirstName());
        users.setLastName(registerDto.getLastName());
        users.setPhone(registerDto.getPhone());
        users.setRole(registerDto.getRole());
        users.setPassword(registerDto.getPassword());
        users.setId(null);
        usersRepository.save(users);
        Users usersFromDb = usersRepository.findByEmail(registerDto.getUsername());
        return usersFromDb.getId();
    }
}
