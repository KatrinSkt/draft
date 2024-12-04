package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Comments;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Integer> {
    List<Comments> findByAdsPk(Integer adsId);

    void deleteByAdsPkAndPk(Integer adId, Integer pk);

    Comments findByAdsPkAndPk(Integer adsId, Integer pk);

}
