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
            "(e.initiator.id in :#{#parameters.users} or :#{#parameters.users} is null) and " +
            "(e.state in :#{#parameters.states} or :#{#parameters.states} is null) and " +
            "(e.category.id in :#{#parameters.categories} or :#{#parameters.categories} is null) and " +
            "e.eventDate between :#{#parameters.rangeStart} and :#{#parameters.rangeEnd}")
    Page<Event> getFilteredEvents(@Param("parameters") AdminEventsParameters parameters, PageRequest pageRequest);

    @Query("SELECT e FROM Event e " +
            "WHERE " +
            "e.state = 'PUBLISHED' and " +
            "(upper(e.annotation) LIKE %:#{#parameters.text}% or upper(e.description) LIKE %:#{#parameters.text}% or " +
            ":#{#parameters.text} is null) and " +
            "(e.category.id in :#{#parameters.categories} or :#{#parameters.categories} is null) and " +
            "(e.paid = :#{#parameters.paid} or :#{#parameters.paid} is null) and " +
            "e.eventDate between :#{#parameters.rangeStart} and :#{#parameters.rangeEnd}")
    Page<Event> getPublicEvents(@Param("parameters") PublicEventsParameters parameters, PageRequest pageRequest);

    @Query("SELECT e FROM Event e " +
            "WHERE " +
            "e.state = 'PUBLISHED' and " +
            "(e.participantLimit = 0 or e.confirmedRequests < e.participantLimit) and " +
            "(upper(e.annotation) LIKE %:#{#parameters.text}% or upper(e.description) LIKE %:#{#parameters.text}% or " +
            ":#{#parameters.text} is null) and " +
            "(e.category.id in :#{#parameters.categories} or :#{#parameters.categories} is null) and " +
            "(e.paid = :#{#parameters.paid} or :#{#parameters.paid} is null) and " +
            "e.eventDate between :#{#parameters.rangeStart} and :#{#parameters.rangeEnd}")
    Page<Event> getAvailableEvents(@Param("parameters") PublicEventsParameters parameters, PageRequest pageRequest);
}
