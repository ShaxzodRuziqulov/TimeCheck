package com.example.timecheck.service.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TimeTrackDto {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;

    private Long userId;

    private String delayReason;
    private LocalDateTime createdAt;

}
