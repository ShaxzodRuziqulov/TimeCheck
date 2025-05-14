package com.example.timecheck.service.mapper;

import com.example.timecheck.entity.TimeTrack;
import com.example.timecheck.service.dto.TimeTrackDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TimeTrackMapper extends EntityMapper<TimeTrackDto, TimeTrack> {
    @Mapping(source = "user.id", target = "userId")
    TimeTrackDto toDto(TimeTrack timeTrack);

    @Mapping(source = "userId", target = "user.id")
    TimeTrack toEntity(TimeTrackDto timeTrackDto);

}
