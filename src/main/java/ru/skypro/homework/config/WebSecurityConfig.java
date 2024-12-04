package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.UsersRepository;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {
    private final UsersRepository usersRepository;

    public WebSecurityConfig(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Устанавливаем запрос для получения пользователя по email
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT email AS username, password, TRUE AS enabled FROM users WHERE email = ?");

        // Устанавливаем запрос для получения ролей пользователя
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT email AS username, role AS authority FROM users WHERE email = ?");

        return jdbcUserDetailsManager;
    }
    /*@Bean
    public InMemoryUserDetailsManager userDetailsService(String username, PasswordEncoder passwordEncoder) {
        Users users = usersRepository.findByEmail(username);
        UserDetails user =
                User.builder()
                        .username(users.getEmail())
                        .password(users.getPassword())
                        .passwordEncoder(passwordEncoder::encode)
                        .roles(users.getRole().name())
                        .build();
        return new InMemoryUserDetailsManager(user);
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
