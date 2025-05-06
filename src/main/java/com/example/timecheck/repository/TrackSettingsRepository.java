package com.example.timecheck.repository;

import com.example.timecheck.entity.TrackSettings;
import com.example.timecheck.entity.enumirated.TrackSettingsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackSettingsRepository extends JpaRepository<TrackSettings, Long> {

    @Query("select t from TrackSettings t where t.trackSettingsStatus=:trackSettingsStatus")
    List<TrackSettings> findByTrackSettingsStatus(@Param("trackSettingsStatus") TrackSettingsStatus trackSettingsStatus);
}
