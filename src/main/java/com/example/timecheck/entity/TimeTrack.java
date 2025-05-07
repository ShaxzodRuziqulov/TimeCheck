package com.example.timecheck.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "time_track")
@Data
public class TimeTrack extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne
    private User user;

    private String delayReason;
    private String endReason;
}
