package com.example.timecheck.service;

import com.example.timecheck.entity.TimeTrack;
import com.example.timecheck.repository.TimeTrackRepository;
import com.example.timecheck.service.dto.TimeTrackDto;
import com.example.timecheck.service.mapper.TimeTrackMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeTrackService {
    private final TimeTrackMapper timeTrackMapper;
    private final TimeTrackRepository timeTrackRepository;

    public TimeTrackService(TimeTrackMapper timeTrackMapper, TimeTrackRepository timeTrackRepository) {
        this.timeTrackMapper = timeTrackMapper;
        this.timeTrackRepository = timeTrackRepository;
    }

    public TimeTrackDto create(TimeTrackDto timeTrackDto) {
        TimeTrack result = timeTrackMapper.toEntity(timeTrackDto);
        timeTrackRepository.save(result);
        return timeTrackMapper.toDto(result);
    }

    public TimeTrackDto update(TimeTrackDto timeTrackDto) {
        if (timeTrackDto.getId() == null || !timeTrackRepository.existsById(timeTrackDto.getId())) {
            throw new RuntimeException("Attendance not found for update");
        }
        TimeTrack timeTrack = timeTrackMapper.toEntity(timeTrackDto);
        timeTrackRepository.save(timeTrack);
        return timeTrackMapper.toDto(timeTrack);
    }

    public List<TimeTrackDto> findAll() {
        return timeTrackRepository
                .findAll()
                .stream()
                .map(timeTrackMapper::toDto)
                .collect(Collectors.toList());
    }

    public TimeTrackDto findById(Long id) {
        TimeTrack timeTrack = timeTrackRepository.findById(id).orElseThrow(() -> new RuntimeException("TimeTrack not found"));
        return timeTrackMapper.toDto(timeTrack);
    }

    public void delete(Long id) {
        TimeTrack timeTrack = timeTrackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeTrack not found"));
        timeTrackRepository.delete(timeTrack);
    }

}
