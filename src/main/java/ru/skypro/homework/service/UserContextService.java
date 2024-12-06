//package ru.skypro.homework.service;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import ru.skypro.homework.model.Users;
//import ru.skypro.homework.repository.UsersRepository;
//
//@Service
//public class UserContextService {
//    private final UsersRepository usersRepository;
//
//    public UserContextService(UsersRepository usersRepository) {
//        this.usersRepository = usersRepository;
//    }
//
//
//    public UserDetails getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            return (UserDetails) authentication.getPrincipal();
//        }
//        throw new IllegalStateException("No authenticated user found");
//    }
//
//    public Users getCurrentUserFromDb() {
//        return usersRepository.findByEmail(getCurrentUser().getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
//}
package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.UserNotAuthenticatedException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.UsersRepository;

/**
 * Сервис для управления контекстом пользователя.
 * <p>
 * Этот класс предоставляет методы для получения текущего аутентифицированного пользователя
 * и его данных из базы данных.
 * </p>
 */
@Service
public class UserContextService {
    private static final Logger logger = LoggerFactory.getLogger(UserContextService.class);
    private final UsersRepository usersRepository;

    public UserContextService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Получает текущего аутентифицированного пользователя.
     *
     * @return объект UserDetails текущего пользователя.
     * @throws UserNotAuthenticatedException если пользователь не аутентифицирован.
     */
    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Attempt to access current user without authentication.");
            throw new UserNotAuthenticatedException("No authenticated user found");
        }
        logger.info("Current user retrieved: {}", authentication.getName());
        return (UserDetails) authentication.getPrincipal();
    }

    /**
     * Получает текущего аутентифицированного пользователя из базы данных.
     *
     * @return объект Users текущего пользователя.
     * @throws UserNotFoundException если пользователь не найден в базе данных.
     */
    public Users getCurrentUserFromDb() {
        String username = getCurrentUser().getUsername();
        logger.info("Fetching user from database with email: {}", username);

        return usersRepository.findByEmail(username)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", username);
                    return new UserNotFoundException("User not found with email: " + username);
                });
    }
}
