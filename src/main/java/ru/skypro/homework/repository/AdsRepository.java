package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Ads;

import java.util.List;

/**
 * Репозиторий для работы с сущностями объявлений.
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций CRUD
 * с объявлениями в базе данных, а также дополнительные методы поиска.
 * </p>
 */
@Repository
public interface AdsRepository extends JpaRepository<Ads, Integer> {

    /**
     * Находит список объявлений по идентификатору пользователя.
     *
     * @param usersId уникальный идентификатор пользователя.
     * @return список объявлений, принадлежащих указанному пользователю.
     */
    List<Ads> findByUsersId(Integer usersId);
}
