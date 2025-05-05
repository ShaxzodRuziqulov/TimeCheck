package com.example.timecheck.repository;

import com.example.timecheck.entity.TimeTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTrackRepository extends JpaRepository<TimeTrack, Long> {
}
