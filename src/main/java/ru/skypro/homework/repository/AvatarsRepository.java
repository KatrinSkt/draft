package ru.skypro.homework.repository;

import liquibase.pro.packaged.R;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Avatars;

import java.util.List;
import java.util.Set;
@Repository
public interface AvatarsRepository extends JpaRepository<Avatars,Integer> {
    Avatars findByUsersId(Integer userId);

    @Query("SELECT a FROM Avatars a WHERE a.users.id IN :userIds")
    List<Avatars> findByUsersIdIn(@Param("userIds") Set<Integer> userIds);
}
