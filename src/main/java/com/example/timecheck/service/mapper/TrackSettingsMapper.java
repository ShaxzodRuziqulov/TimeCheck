package com.example.timecheck.service.mapper;

import com.example.timecheck.entity.TrackSettings;
import com.example.timecheck.service.dto.TrackSettingsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrackSettingsMapper extends EntityMapper<TrackSettingsDto, TrackSettings> {

    TrackSettings toEntity(TrackSettingsDto trackSettingsDto);

    TrackSettingsDto toDto(TrackSettings trackSettings);
}
