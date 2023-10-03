package ru.practicum.server.comments.service;

import ru.practicum.server.comments.dto.CommentDto;
import ru.practicum.server.comments.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto create(NewCommentDto newCommentDto, int userId, int eventId);

    CommentDto update(NewCommentDto newCommentDto, int userId, int commentId);

    void delete(int commentId, int userId);

    void deleteByAdmin(int commentId);

    List<CommentDto> getEventComments(int eventId, int from, int size);
}
