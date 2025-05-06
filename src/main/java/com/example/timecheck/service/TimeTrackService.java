package com.example.timecheck.service;

import com.example.timecheck.entity.TimeTrack;
import com.example.timecheck.entity.User;
import com.example.timecheck.repository.TimeTrackRepository;
import com.example.timecheck.repository.UserRepository;
import com.example.timecheck.service.dto.TimeTrackDto;
import com.example.timecheck.service.mapper.TimeTrackMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeTrackService {
    private final TimeTrackMapper timeTrackMapper;
    private final TimeTrackRepository timeTrackRepository;
    private final UserRepository userRepository;

    public TimeTrackService(TimeTrackMapper timeTrackMapper, TimeTrackRepository timeTrackRepository, UserRepository userRepository) {
        this.timeTrackMapper = timeTrackMapper;
        this.timeTrackRepository = timeTrackRepository;
        this.userRepository = userRepository;
    }

    public TimeTrackDto create(TimeTrackDto timeTrackDto) {
        TimeTrack result = timeTrackMapper.toEntity(timeTrackDto);
        timeTrackRepository.save(result);
        return timeTrackMapper.toDto(result);
    }

    public TimeTrackDto update(TimeTrackDto timeTrackDto) {
        TimeTrack result = timeTrackMapper.toEntity(timeTrackDto);

        if (timeTrackDto.getUserId() != null) {
            User user = userRepository.findById(timeTrackDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            result.setUser(user);
        }
        timeTrackRepository.save(result);
        return timeTrackMapper.toDto(result);
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
