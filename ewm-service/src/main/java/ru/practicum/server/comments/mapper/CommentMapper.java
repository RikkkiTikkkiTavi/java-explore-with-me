package ru.practicum.server.comments.mapper;

import ru.practicum.server.comments.dto.CommentDto;
import ru.practicum.server.comments.dto.NewCommentDto;
import ru.practicum.server.comments.model.Comment;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment newCommentToComment(NewCommentDto dto, Event event, User creator) {
        return Comment.builder()
                .text(dto.getText())
                .event(event)
                .creator(creator)
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentDto commentToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .created(comment.getCreated())
                .creator(comment.getCreator())
                .text(comment.getText())
                .event(comment.getEvent())
                .build();
    }
}