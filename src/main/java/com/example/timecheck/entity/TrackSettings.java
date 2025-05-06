package com.example.timecheck.entity;

import com.example.timecheck.entity.enumirated.TrackSettingsStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
public class TrackSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime fromTime;
    private LocalTime toTime;

    @Enumerated(EnumType.STRING)
    private TrackSettingsStatus trackSettingsStatus;
}
