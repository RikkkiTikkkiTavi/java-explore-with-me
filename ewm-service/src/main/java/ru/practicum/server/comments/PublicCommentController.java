package ru.practicum.server.comments;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comments.dto.CommentDto;
import ru.practicum.server.comments.service.CommentService;

import java.util.List;

@RestController
@RequestMapping(path = "/comments")
@AllArgsConstructor
@Validated
public class PublicCommentController {

    CommentService commentService;

    @GetMapping("/{eventId}")
    public List<CommentDto> getEventComments(@PathVariable int eventId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return commentService.getEventComments(eventId, from, size);
    }
}
