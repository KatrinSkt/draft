package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentController {

    private final CommentService commentService; // Сервис для работы с комментариями

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}/comments")
    @Operation(summary = "Получение комментариев объявления")
    public CommentsDto getComments(@PathVariable Integer id) {
        // Логика получения комментариев для объявления
        return commentService.getCommentsForAd(id);
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Добавление комментария к объявлению")
    public CommentDto addComment(@PathVariable Integer adId, @RequestBody CreateOrUpdateCommentDto comment) {
        // Логика добавления комментария к объявлению
        return commentService.addComment(adId, comment);
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Обновление комментария")
    public CommentDto updateComment(@PathVariable Integer adId, @PathVariable Integer commentId,
                                    @RequestBody CreateOrUpdateCommentDto comment) {
        // Логика обновления комментария

        return commentService.updateComment(adId, commentId, comment);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Удаление комментария")
    public void deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
        // Логика удаления комментария
        commentService.deleteComment(adId, commentId);
    }
}