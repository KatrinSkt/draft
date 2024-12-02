package ru.skypro.homework.repository;

import liquibase.pro.packaged.I;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Avatars;
import ru.skypro.homework.model.Users;

public interface AvatarsRepository extends JpaRepository<Avatars,Integer> {
    Avatars findByUsersId(Integer userId);
}
