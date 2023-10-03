package ru.practicum.server.comments.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.comments.dto.CommentDto;
import ru.practicum.server.comments.dto.NewCommentDto;
import ru.practicum.server.comments.mapper.CommentMapper;
import ru.practicum.server.comments.model.Comment;
import ru.practicum.server.comments.repository.CommentRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.exception.ConflictException;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.exception.ValidateException;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;

    @Transactional
    @Override
    public CommentDto create(NewCommentDto newDto, int userId, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event state must be published");
        }
        User creator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Comment comment = commentRepository.save(CommentMapper.newCommentToComment(newDto, event, creator));
        return CommentMapper.commentToDto(comment);
    }

    @Transactional
    @Override
    public CommentDto update(NewCommentDto newDto, int userId, int commentId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (comment.getCreator().getId() != userId) {
            throw new ValidateException("You are not the author of the comment");
        }
        comment.setText(newDto.getText());
        return CommentMapper.commentToDto(comment);
    }

    @Transactional
    @Override
    public void delete(int commentId, int userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        if (comment.getCreator().getId() != userId) {
            throw new ValidateException("You are not the author of the comment");
        }
        commentRepository.deleteById(commentId);
    }

    @Transactional
    @Override
    public void deleteByAdmin(int commentId) {
        commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getEventComments(int eventId, int from, int size) {
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "created"));
        List<Comment> comments = commentRepository.findAllByEvent_Id(eventId, pageRequest);
        return comments.stream().map(CommentMapper::commentToDto).collect(Collectors.toList());
    }
}