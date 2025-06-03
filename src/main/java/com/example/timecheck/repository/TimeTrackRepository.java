package com.example.timecheck.repository;

import com.example.timecheck.entity.TimeTrack;
import com.example.timecheck.service.dto.TimeTrackUserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeTrackRepository extends JpaRepository<TimeTrack, Long>, JpaSpecificationExecutor<TimeTrack> {
    Optional<TimeTrack> findFirstByUserIdAndDateBetweenOrderByStartTimeAsc(
            Long user_id, LocalDate date, LocalDate date2);

    Optional<TimeTrack> findByUserIdAndDateAndEndTimeIsNull(Long userId, LocalDate date);


    @Query(value = "SELECT tt.id, " +
            "tt.start_time, " +
            "tt.end_time, " +
            "tt.user_id, " +
            "tt.delay_reason, " +
            "tt.end_reason, " +
            "tt.date, " +
            "u.first_name, " +
            "u.last_name, " +
            "u.middle_name," +
            "u.birth_date," +
            "u.user_name," +
            "u.password " +
            "FROM time_track tt " +
            "JOIN users u ON tt.user_id = u.id " +
            "WHERE tt.user_id = :userId", nativeQuery = true)
    List<TimeTrackUserDto> getAllWithUserInfoByUserId(@Param("userId") Long userId);


    boolean existsByUserIdAndDate(Long user_id, LocalDate date);

    Optional<TimeTrack> findByUserIdAndDate(Long userId, LocalDate today);

}
