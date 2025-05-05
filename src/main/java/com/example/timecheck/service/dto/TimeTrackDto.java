package com.example.timecheck.service.dto;

import com.example.timecheck.entity.enumirated.TimeTrackStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimeTrackDto {
    private Long id;
    private LocalDate date;
    private LocalTime checkinTime;
    private boolean confirmedByInspector;

    private Long userId;
    private String reason;

    private TimeTrackStatus timeTrackStatus;
}
