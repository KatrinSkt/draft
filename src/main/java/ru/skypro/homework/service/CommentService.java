package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.mapper.Mappers;
import ru.skypro.homework.model.Avatars;
import ru.skypro.homework.model.Comments;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.AvatarsRepository;
import ru.skypro.homework.repository.CommentsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final Mappers mappers;
    private final AvatarsRepository avatarsRepository;
    private final CommentsRepository commentsRepository;
    private final AdsRepository adsRepository;

    public CommentService(AvatarsRepository avatarsRepository,
                          CommentsRepository commentsRepository,
                          AdsRepository adsRepository,
                          Mappers mappers) {
        this.avatarsRepository = avatarsRepository;
        this.commentsRepository = commentsRepository;
        this.adsRepository = adsRepository;
        this.mappers = mappers;
    }

    public CommentsDto getCommentsForAd(Integer adId) {

// Получаем список комментариев для данного объявления
        List<Comments> commentsList = commentsRepository.findByAdsPk(adId);

        // Создаем CommentsDto и устанавливаем количество комментариев
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setCount(commentsList.size());

        // Получаем список всех уникальных пользователей из комментариев
        Set<Integer> userIds = commentsList.stream()
                .map(comment -> comment.getUsers().getId())
                .collect(Collectors.toSet());

        // Загружаем аватары для всех пользователей за один запрос
        Map<Integer, String> avatarsMap = avatarsRepository.findByUsersIdIn(userIds)
                .stream()
                .collect(Collectors.toMap(Avatars::getId, Avatars::getFilePath));

        // Преобразуем список комментариев в список CommentDto
        List<CommentDto> commentDtoList = commentsList.stream()
                .map(comment -> new CommentDto(
                        comment.getUsers().getId(),
                        avatarsMap.get(comment.getUsers().getId()),  // Получаем путь к аватару из карты
                        comment.getUsers().getFirstName(),
                        comment.getCreatedAt(),
                        comment.getPk(),
                        comment.getText()
                ))
                .collect(Collectors.toList());

        // Устанавливаем результаты в CommentsDto
        commentsDto.setResults(commentDtoList);

        return commentsDto;







///////////////    ТУТ НАПИСАН ПЕРВОНАЧАЛЬНЫЙ ВАРИАНТ КОДА ///////////////////////////
//        List<Comments> commentsList = commentsRepository.findByAdsPk(adId);
//        CommentsDto commentsDto = new CommentsDto();
//        commentsDto.setCount(commentsList.size());
//        List<CommentDto> commentDtoList = new ArrayList<>();
//        for (int i = 0; i < commentsList.size(); i++) {
//            commentDtoList.add(new CommentDto(
//                    commentsList.get(i).getUsers().getId(),
//                    avatarsRepository.findByUsersId(commentsList.get(i).getUsers().getId()).getFilePath(),
//                    commentsList.get(i).getUsers().getFirstName(),
//                    commentsList.get(i).getCreatedAt(),
//                    commentsList.get(i).getPk(),
//                    commentsList.get(i).getText()
//
//            ));
//        }
//
//        commentsDto.setResults(commentDtoList);
//        return commentsDto;
  }

    public CommentDto addComment(Integer adId, CreateOrUpdateCommentDto commentDto) {

        Comments comments = mappers.toComments(commentDto);

//        Comments comments = new Comments();
//        comments.setPk(null);
//        comments.setText(commentDto.getText());
//        comments.setCreatedAt(System.currentTimeMillis()); // Дата создания
        comments.setAds(adsRepository.findById(adId).get()); // Не совсем правильно может вернуться null, обработать Optional
        comments.setUsers(adsRepository.findById(adId).get().getUsers()); // Нужно посмотреть может переделать


        Comments commentsFromDb = commentsRepository.save(comments);

        CommentDto commentDtoFromDb = mappers.toCommentDto(commentsFromDb);
        //       CommentDto commentDtoFromDb = new CommentDto();


        commentDtoFromDb.setAuthorImage(avatarsRepository.findByUsersId(commentsFromDb.getUsers().getId()).getFilePath());
        //       commentDtoFromDb.setAuthor(commentsFromDb.getUsers().getId());
//        commentDtoFromDb.setAuthorFirstName(commentsFromDb.getUsers().getFirstName());
//        commentDtoFromDb.setPk(commentsFromDb.getPk());
//        commentDtoFromDb.setText(commentsFromDb.getText());
//        commentDtoFromDb.setCreatedAt(commentsFromDb.getCreatedAt());

        return commentDtoFromDb;
    }

    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto commentDto) {
        Comments comments = commentsRepository.findByAdsPkAndPk(adId, commentId);
        comments.setText(commentDto.getText());
        Comments commentsFromDb = commentsRepository.save(comments);

        CommentDto commentDtoFromDb = mappers.toCommentDto(commentsFromDb);
//        CommentDto commentDtoFromDb = new CommentDto();

//        commentDtoFromDb.setAuthor(commentsFromDb.getPk());
        commentDtoFromDb.setAuthorImage(avatarsRepository.findByUsersId(commentsFromDb.getUsers().getId()).getFilePath());
//        commentDtoFromDb.setAuthorFirstName(commentsFromDb.getUsers().getFirstName());
//        commentDtoFromDb.setPk(commentsFromDb.getPk());
//        commentDtoFromDb.setText(commentsFromDb.getText());
//        commentDtoFromDb.setCreatedAt(commentsFromDb.getCreatedAt());
        return commentDtoFromDb;
    }

    public void deleteComment(Integer adId, Integer commentId) {
        commentsRepository.deleteByAdsPkAndPk(adId, commentId);
    }


}