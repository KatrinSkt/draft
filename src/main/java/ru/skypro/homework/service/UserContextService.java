package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.UsersRepository;

@Service
public class UserContextService {
    private final UsersRepository usersRepository;

    public UserContextService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (UserDetails) authentication.getPrincipal();
        }
        throw new IllegalStateException("No authenticated user found");
    }

    public Users getCurrentUserFromDb() {
        return usersRepository.findByEmail(getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
