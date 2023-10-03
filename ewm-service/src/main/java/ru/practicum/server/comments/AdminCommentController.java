package ru.practicum.server.comments;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comments.service.CommentService;

@RestController
@RequestMapping(path = "/admin/comments")
@AllArgsConstructor
@Validated
public class AdminCommentController {

    CommentService commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByAdmin(@PathVariable int commentId) {
        commentService.deleteByAdmin(commentId);
    }
}
