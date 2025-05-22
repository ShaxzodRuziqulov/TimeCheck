package com.example.timecheck.repository;

import com.example.timecheck.entity.TimeTrack;
import com.example.timecheck.service.dto.TimeTrackUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeTrackRepository extends JpaRepository<TimeTrack, Long> {
    Optional<TimeTrack> findFirstByUserIdAndDateBetweenOrderByStartTimeAsc(
            Long user_id, LocalDate date, LocalDate date2);

    Optional<TimeTrack> findByUserIdAndDateAndEndTimeIsNull(Long userId, LocalDate date);

    List<TimeTrack> findAllByDateBetweenAndEndTimeIsNull(LocalDate from, LocalDate to);


    @Query(value = "SELECT tt.id, " +
            "tt.start_time, " +
            "tt.end_time, " +
            "tt.user_id, " +
            "tt.delay_reason, " +
            "tt.end_reason, " +
            "tt.date, " +
            "u.first_name, " +
            "u.last_name ," +
            "u.middle_name " +
            "FROM time_track tt " +
            "JOIN users u ON tt.user_id = u.id", nativeQuery = true)
    List<TimeTrackUserDto> getAllWithUserInfo();

    boolean existsByUserIdAndDate(Long user_id, LocalDate date);

    Optional<TimeTrack> findByUserIdAndDate(Long userId, LocalDate today);

    Page<TimeTrack> findAllByUserId(Long user, Pageable pageable);

}
