package com.example.timecheck.repository;

import com.example.timecheck.entity.TimeTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeTrackRepository extends JpaRepository<TimeTrack, Long> {
    Optional<TimeTrack> findFirstByUserIdAndCreatedAtBetweenOrderByStartTimeAsc(
            Long userId, LocalDateTime from, LocalDateTime to);

    Optional<TimeTrack> findByUserIdAndEndTimeIsNull(Long userId);

    List<TimeTrack> findAllByCreatedAtBetweenAndEndTimeIsNull(LocalDateTime from, LocalDateTime to);

    boolean existsByUserIdAndStartTimeBetween(Long user_id, LocalTime startTime, LocalTime startTime2);

    @Query(value = "select tt.id,\n" +
            "       tt.start_time,\n" +
            "       tt.end_time,\n" +
            "       tt.user_id,\n" +
            "       tt.delay_reason,\n" +
            "       tt.end_reason,\n" +
            "       tt.created_at,\n" +
            "       u.first_name,\n" +
            "       u.last_name\n" +
            "from time_track tt\n" +
            "         join users u on tt.user_id = u.id\n", nativeQuery = true)
    List<TimeTrack> findAllByUserAndTimeTrack();
}
