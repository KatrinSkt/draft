package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private List<CommentDto> commentDtoList = new ArrayList<>(); // Хранение комментариев в памяти (можно заменить на БД)
    private int currentId = 1; // Счетчик ID для новых комментариев

    public CommentsDto getCommentsForAd(Integer adId) {
        CommentsDto commentsDto = new CommentsDto();
//        // Фильтрация комментариев по ID объявления
//        List<Comment> adComments = new ArrayList<>();
//        for (Comment comment : commentList) {
//            if (comment.getAdId().equals(adId)) {
//                adComments.add(comment);
//            }
//        }
//        comments.setComments(adComments);
        return commentsDto;
    }

    public CommentDto addComment(Integer adId, CreateOrUpdateCommentDto commentDto) {
        CommentDto newCommentDto = new CommentDto();
//        newComment.setPk(currentId++);
//        newComment.setAdId(adId);
//        newComment.setText(commentDto.getText());
//
//        // Сохранение нового комментария в списке
//        commentList.add(newComment);
        return newCommentDto;
    }

    public void updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto commentDto) {
        CommentDto existingCommentDto = getCommentById(commentId);
        if (!existingCommentDto.getPk().equals(adId)) {
            throw new RuntimeException("Комментарий не принадлежит данному объявлению");
        }
        existingCommentDto.setText(commentDto.getText());
    }

    public void deleteComment(Integer adId, Integer commentId) {
        CommentDto commentDtoToDelete = getCommentById(commentId);
        if (!commentDtoToDelete.getPk().equals(adId)) {
            throw new RuntimeException("Комментарий не принадлежит данному объявлению");
        }
        commentDtoList.remove(commentDtoToDelete);
    }

    private CommentDto getCommentById(Integer id) {
        Optional<CommentDto> commentOptional = commentDtoList.stream().filter(commentDto -> commentDto.getPk().equals(id)).findFirst();
        if (commentOptional.isPresent()) {
            return commentOptional.get();
        }
        throw new RuntimeException("Комментарий не найден");
    }
}