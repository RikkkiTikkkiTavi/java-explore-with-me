package ru.practicum.server.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.server.category.model.Category;
import ru.practicum.server.location.model.Location;
import ru.practicum.server.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    LocalDateTime eventDate;

    @Column(name = "published")
    LocalDateTime publishedOn;

    @Column(name = "created")
    LocalDateTime createdOn;

    @OneToOne
    @JoinColumn(name = "location_id")
    Location location;

    @Column(name = "paid")
    Boolean paid;

    @Column(name = "participant_limit")
    int participantLimit;

    @Column(name = "request_moderation")
    boolean requestModeration;

    @Column(name = "title")
    String title;

    @OneToOne
    @JoinColumn(name = "initiator_id")
    User initiator;

    @Column(name = "confirmed_requests")
    int confirmedRequests;

    @Enumerated(EnumType.STRING)
    private EventState state;
}
