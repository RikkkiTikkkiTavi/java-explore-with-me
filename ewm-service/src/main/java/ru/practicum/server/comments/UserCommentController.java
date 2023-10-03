package ru.practicum.server.comments;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comments.dto.CommentDto;
import ru.practicum.server.comments.dto.NewCommentDto;
import ru.practicum.server.comments.service.CommentService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users/{userId}/comments")
@AllArgsConstructor
@Validated
public class UserCommentController {

    private CommentService commentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable("userId") int userId,
                             @PathVariable("eventId") int eventId,
                             @RequestBody @Valid NewCommentDto newCommentDto) {
        return commentService.create(newCommentDto, userId, eventId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@RequestBody @Valid NewCommentDto newCommentDto,
                             @PathVariable("userId") int userId,
                             @PathVariable int commentId) {
        return commentService.update(newCommentDto, userId, commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int commentId,
                       @PathVariable("userId") int userId) {
        commentService.delete(commentId, userId);
    }
}
