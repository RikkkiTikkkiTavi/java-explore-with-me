package ru.practicum.server.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByInitiator_Id(int initiatorId, PageRequest pr);

    Event findFirstByIdAndInitiator_Id(int id, int initiatorId);

    List<Event> findByIdIn(List<Integer> ids);

    @Query("SELECT e FROM Event e " +
            "WHERE " +
            "(e.initiator.id in :users or :users is null) and " +
            "(e.state in :states or :states is null) and " +
            "(e.category.id in :categories or :categories is null) and " +
            "e.eventDate between :rangeStart and :rangeEnd")
    Page<Event> getFilteredEvents(List<Integer> users, List<EventState> states, List<Integer> categories,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("SELECT e FROM Event e " +
            "WHERE " +
            "e.state = 'PUBLISHED' and " +
            "(upper(e.annotation) LIKE %:text% or upper(e.description) LIKE %:text% or :text is null) and " +
            "(e.category.id in :categories or :categories is null) and " +
            "(e.paid = :paid or :paid is null) and " +
            "e.eventDate between :rangeStart and :rangeEnd")
    Page<Event> getPublicEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("SELECT e FROM Event e " +
            "WHERE " +
            "e.state = 'PUBLISHED' and " +
            "(e.participantLimit = 0 or e.confirmedRequests < e.participantLimit) and " +
            "(upper(e.annotation) LIKE %:text% or upper(e.description) LIKE %:text% or :text is null) and " +
            "(e.category.id in :categories or :categories is null) and " +
            "(e.paid = :paid or :paid is null) and " +
            "e.eventDate between :rangeStart and :rangeEnd")
    Page<Event> getAvailableEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, PageRequest pageRequest);
}
