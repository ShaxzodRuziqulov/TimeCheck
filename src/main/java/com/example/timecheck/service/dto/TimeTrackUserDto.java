package com.example.timecheck.service.dto;


import java.time.LocalDate;
import java.time.LocalTime;


public interface TimeTrackUserDto {
    Long getId();

    LocalTime getStartTime();

    LocalTime getEndTime();

    Long getUserId();

    String getDelayReason();

    String getEndReason();

    LocalDate getDate();

    String getFirstName();

    String getLastName();

    String getMiddleName();

    String getBirthDate();
    String getPassword();
    String getUserName();
}

