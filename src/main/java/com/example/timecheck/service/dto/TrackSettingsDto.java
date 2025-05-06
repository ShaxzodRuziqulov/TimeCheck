package com.example.timecheck.service.dto;

import com.example.timecheck.entity.enumirated.TrackSettingsStatus;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TrackSettingsDto {
    private Long id;
    private LocalTime fromTime;
    private LocalTime toTime;

    private TrackSettingsStatus trackSettingsStatus;


}
