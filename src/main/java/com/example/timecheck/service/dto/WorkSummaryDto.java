package com.example.timecheck.service.dto;

import lombok.Data;

import java.time.LocalTime;
@Data
public class WorkSummaryDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private String workedHours;
    private boolean canLeave;
}
