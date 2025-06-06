package com.example.timecheck.service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TimeTrackDto {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long userId;
    private String delayReason;
    private String endReason;
    private LocalDate date;
    private LocalDateTime createdAt;
}
