package ru.practicum.server.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.server.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByInitiator_Id(int initiatorId, PageRequest pr);

    Event findFirstByIdAndInitiator_Id(int id, int initiatorId);

    List<Event> findByIdIn(List<Integer> ids);

    @Query("SELECT e FROM Event e " +
            "WHERE " +
            "(e.initiator.id in :#{#param.users} or :#{#param.users} is null) and " +
            "(e.state in :#{#param.states} or :#{#param.states} is null) and " +
            "(e.category.id in :#{#param.categories} or :#{#param.categories} is null) and " +
            "e.eventDate between :#{#param.rangeStart} and :#{#param.rangeEnd}")
    Page<Event> getFilteredEvents(AdminEventsParameters param, PageRequest pageRequest);

    @Query("SELECT e FROM Event e " +
            "WHERE " +
            "e.state = 'PUBLISHED' and " +
            "(upper(e.annotation) LIKE %:#{#param.text}% or upper(e.description) LIKE %:#{#param.text}% or " +
            ":#{#param.text} is null) and " +
            "(e.category.id in :#{#param.categories} or :#{#param.categories} is null) and " +
            "(e.paid = :#{#param.paid} or :#{#param.paid} is null) and " +
            "e.eventDate between :#{#param.rangeStart} and :#{#param.rangeEnd}")
    Page<Event> getPublicEvents(@Param("param") PublicEventsParameters param, PageRequest pageRequest);

    @Query("SELECT e FROM Event e " +
            "WHERE " +
            "e.state = 'PUBLISHED' and " +
            "(e.participantLimit = 0 or e.confirmedRequests < e.participantLimit) and " +
            "(upper(e.annotation) LIKE %:#{#param.text}% or upper(e.description) LIKE %:#{#param.text}% or " +
            ":#{#param.text} is null) and " +
            "(e.category.id in :#{#param.categories} or :#{#param.categories} is null) and " +
            "(e.paid = :#{#param.paid} or :#{#param.paid} is null) and " +
            "e.eventDate between :#{#param.rangeStart} and :#{#param.rangeEnd}")
    Page<Event> getAvailableEvents(@Param("param") PublicEventsParameters param, PageRequest pageRequest);
}
