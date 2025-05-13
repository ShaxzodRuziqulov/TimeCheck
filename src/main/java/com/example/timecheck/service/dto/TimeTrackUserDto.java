package com.example.timecheck.service.dto;


import java.time.LocalDateTime;
import java.time.LocalTime;


public interface TimeTrackUserDto {
    Long getId();

    LocalTime getStartTime();

    LocalTime getEndTime();

    Long getUserId();

    String getDelayReason();

    String getEndReason();

    LocalDateTime getCreatedAt();

    String getFirstName();

    String getLastName();

    String getMiddleName();
}

