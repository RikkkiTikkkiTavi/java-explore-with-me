package ru.practicum.server.comments.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "text")
    String text;

    @OneToOne
    @JoinColumn(name = "creator_id")
    User creator;

    @OneToOne
    @JoinColumn(name = "event_id")
    Event event;

    @Column(name = "created")
    LocalDateTime created;
}
