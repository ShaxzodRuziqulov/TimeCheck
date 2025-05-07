package com.example.timecheck.repository;

import com.example.timecheck.entity.TimeTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeTrackRepository extends JpaRepository<TimeTrack, Long> {
    Optional<TimeTrack> findFirstByUserIdAndCreatedAtBetweenOrderByStartTimeAsc(
            Long userId, LocalDateTime from, LocalDateTime to);

    Optional<TimeTrack> findByUserIdAndEndTimeIsNull(Long userId);

    List<TimeTrack> findAllByCreatedAtBetweenAndEndTimeIsNull(LocalDateTime from, LocalDateTime to);


}
