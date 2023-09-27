package ru.practicum.server.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.request.model.ParticipationRequest;
import ru.practicum.server.request.model.Status;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Integer> {
    Optional<ParticipationRequest> findByRequester_IdAndEvent_Id(int requesterId, int eventId);

    List<ParticipationRequest> findAllByEvent_Id(int eventId);

    List<ParticipationRequest> findAllByRequester_Id(int requesterId);

    List<ParticipationRequest> findAllByIdInAndStatus(List<Integer> ids, Status status);

    @Modifying
    @Query("update ParticipationRequest p SET " +
            "p.status = :status " +
            "where p.id in :ids and p.status = :oldStatus")
    void updateRequests(List<Integer> ids, Status status, Status oldStatus);
}
