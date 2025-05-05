package com.example.timecheck.entity;

import com.example.timecheck.entity.enumirated.TimeTrackStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "time_track")
@Data
public class TimeTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private LocalTime checkinTime;
    private boolean confirmedByInspector;

    @ManyToOne
    private User user;
    private String reason;

    @Enumerated(EnumType.STRING)
    private TimeTrackStatus timeTrackStatus;

}
