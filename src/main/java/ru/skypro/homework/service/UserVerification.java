//package ru.skypro.homework.service;
//
//import org.springframework.stereotype.Service;
//import ru.skypro.homework.model.Ads;
//import ru.skypro.homework.model.Comments;
//import ru.skypro.homework.repository.AdsRepository;
//import ru.skypro.homework.repository.CommentsRepository;
//
//@Service
//public class UserVerification {
//    private final AdsRepository adsRepository;
//    private final UserContextService userContextService;
//    private final CommentsRepository commentsRepository;
//
//    public UserVerification(AdsRepository adsRepository, UserContextService userContextService, CommentsRepository commentsRepository) {
//        this.adsRepository = adsRepository;
//        this.userContextService = userContextService;
//        this.commentsRepository = commentsRepository;
//    }
//
//    public boolean verificationUserForComment(Integer commentId) {
//        boolean flag = false;
//        Comments comments = commentsRepository.findById(commentId).get();
//        if(comments.getUsers().getId().equals(userContextService.getCurrentUserFromDb().getId())){
//            flag = true;
//        }
//        return flag;
//    }
//    public boolean verificationUserForAds(Integer id) {
//        boolean flag = false;
//        Ads ads = adsRepository.findById(id).get();
//        if(ads.getUsers().getId().equals(userContextService.getCurrentUserFromDb().getId())){
//            flag = true;
//        }
//        return flag;
//    }
//
//
//}
package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comments;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentsRepository;

/**
 * Сервис для проверки прав пользователя на доступ к объявлениям и комментариям.
 */
@Service
public class UserVerification {

    private static final Logger logger = LoggerFactory.getLogger(UserVerification.class);

    private final AdsRepository adsRepository;
    private final UserContextService userContextService;
    private final CommentsRepository commentsRepository;

    public UserVerification(AdsRepository adsRepository, UserContextService userContextService, CommentsRepository commentsRepository) {
        this.adsRepository = adsRepository;
        this.userContextService = userContextService;
        this.commentsRepository = commentsRepository;
    }

    /**
     * Проверяет, имеет ли текущий пользователь право редактировать комментарий.
     *
     * @param commentId уникальный идентификатор комментария.
     * @return true, если пользователь имеет право редактировать комментарий; иначе false.
     * @throws CommentNotFoundException если комментарий не найден.
     */
    public boolean verificationUserForComment(Integer commentId) {
        logger.info("Verifying user permissions for comment ID: {}", commentId);

        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> {
                    logger.error("Comment not found with ID: {}", commentId);
                    return new CommentNotFoundException("Comment not found with ID: " + commentId);
                });

        boolean hasPermission = comment.getUsers().getId().equals(userContextService.getCurrentUserFromDb().getId());
        logger.info("User {} has permission to edit comment ID: {}", userContextService.getCurrentUserFromDb().getId(), commentId);

        return hasPermission;
    }

    /**
     * Проверяет, имеет ли текущий пользователь право редактировать объявление.
     *
     * @param adId уникальный идентификатор объявления.
     * @return true, если пользователь имеет право редактировать объявление; иначе false.
     * @throws AdNotFoundException если объявление не найдено.
     */
    public boolean verificationUserForAds(Integer adId) {
        logger.info("Verifying user permissions for ad ID: {}", adId);

        Ads ad = adsRepository.findById(adId)
                .orElseThrow(() -> {
                    logger.error("Ad not found with ID: {}", adId);
                    return new AdNotFoundException("Ad not found with ID: " + adId);
                });

        boolean hasPermission = ad.getUsers().getId().equals(userContextService.getCurrentUserFromDb().getId());
        logger.info("User {} has permission to edit ad ID: {}", userContextService.getCurrentUserFromDb().getId(), adId);

        return hasPermission;
    }
}
