package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> searchAllByRequestor_IdOrderByCreatedDesc(Long requestorId);

    List<Request> searchAllByRequestor_IdNot(Long requestorId, Pageable pageable);
}
