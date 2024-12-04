package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Images;
@Repository
public interface ImagesRepository extends JpaRepository<Images,Integer> {
    Images findByAdsPk(Integer adsId);
}
