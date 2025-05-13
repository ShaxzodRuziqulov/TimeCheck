package com.example.timecheck.repository;

import com.example.timecheck.entity.TimeTrack;
import com.example.timecheck.service.dto.TimeTrackUserDto;
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

    @Query(value = "SELECT tt.id, " +
            "tt.start_time, " +
            "tt.end_time, " +
            "tt.user_id, " +
            "tt.delay_reason, " +
            "tt.end_reason, " +
            "tt.created_at, " +
            "u.first_name, " +
            "u.last_name ," +
            "u.middle_name " +
            "FROM time_track tt " +
            "JOIN users u ON tt.user_id = u.id", nativeQuery = true)
    List<TimeTrackUserDto> getAllWithUserInfo();
}
