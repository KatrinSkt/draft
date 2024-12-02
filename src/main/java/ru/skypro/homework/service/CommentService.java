package ru.skypro.homework.service;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.model.Comments;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.AvatarsRepository;
import ru.skypro.homework.repository.CommentsRepository;
import ru.skypro.homework.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final AvatarsRepository avatarsRepository;
    private final CommentsRepository commentsRepository;
    private final AdsRepository adsRepository;
    private final UsersRepository usersRepository;

    public CommentService(AvatarsRepository avatarsRepository,
                          CommentsRepository commentsRepository,
                          AdsRepository adsRepository,
                          UsersRepository usersRepository) {
        this.avatarsRepository = avatarsRepository;
        this.commentsRepository = commentsRepository;
        this.adsRepository = adsRepository;
        this.usersRepository = usersRepository;
    }

    public CommentsDto getCommentsForAd(Integer adId) {
        List<Comments> commentsList = commentsRepository.findByAdsId(adId);
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setCount(commentsList.size());
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (int i = 0; i < commentsList.size(); i++) {
            commentDtoList.add(new CommentDto(
                    commentsList.get(i).getUsers().getId(),
                    avatarsRepository.findByUsersId(commentsList.get(i).getUsers().getId()).getFilePath(),
                    commentsList.get(i).getUsers().getFirstName(),
                    commentsList.get(i).getCreatedAt(),
                    commentsList.get(i).getPk(),
                    commentsList.get(i).getText()

            ));
        }

        commentsDto.setResults(commentDtoList);
        return commentsDto;
    }

    public CommentDto addComment(Integer adId, CreateOrUpdateCommentDto commentDto) {
        Comments comments = new Comments();
        comments.setPk(null);
        comments.setText(commentDto.getText());
        comments.setCreatedAt(System.currentTimeMillis()); // Дата создания
        comments.setAds(adsRepository.findById(adId).get()); // Не совсем правильно может вернуться null, обработать Optional
        comments.setUsers(adsRepository.findById(adId).get().getUsers()); // Нужно посмотреть может переделать
        Comments commentsFromDb = commentsRepository.save(comments);

        CommentDto commentDtoFromDb = new CommentDto();
        commentDtoFromDb.setAuthor(commentsFromDb.getPk());
        commentDtoFromDb.setAuthorImage(avatarsRepository.findByUsersId(commentsFromDb.getUsers().getId()).getFilePath());
        commentDtoFromDb.setAuthorFirstName(commentsFromDb.getUsers().getFirstName());
        commentDtoFromDb.setPk(commentsFromDb.getPk());
        commentDtoFromDb.setText(commentsFromDb.getText());
        commentDtoFromDb.setCreatedAt(commentsFromDb.getCreatedAt());
        return commentDtoFromDb;
    }

    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto commentDto) {
        Comments comments = commentsRepository.findByAdsIdAndPk(adId, commentId);
        comments.setText(commentDto.getText());
        Comments commentsFromDb =commentsRepository.save(comments);
        CommentDto commentDtoFromDb = new CommentDto();
        commentDtoFromDb.setAuthor(commentsFromDb.getPk());
        commentDtoFromDb.setAuthorImage(avatarsRepository.findByUsersId(commentsFromDb.getUsers().getId()).getFilePath());
        commentDtoFromDb.setAuthorFirstName(commentsFromDb.getUsers().getFirstName());
        commentDtoFromDb.setPk(commentsFromDb.getPk());
        commentDtoFromDb.setText(commentsFromDb.getText());
        commentDtoFromDb.setCreatedAt(commentsFromDb.getCreatedAt());
        return commentDtoFromDb;
    }

    public void deleteComment(Integer adId, Integer commentId) {
        commentsRepository.deleteByAdsIdAndPk(adId, commentId);
    }


}