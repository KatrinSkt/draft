package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Users;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {
    Users findByEmail(String username);

}
