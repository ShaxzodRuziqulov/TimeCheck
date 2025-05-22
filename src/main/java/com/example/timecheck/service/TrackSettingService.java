package com.example.timecheck.service;

import com.example.timecheck.entity.TrackSettings;
import com.example.timecheck.entity.enumirated.TrackSettingsStatus;
import com.example.timecheck.repository.TrackSettingsRepository;
import com.example.timecheck.service.dto.TrackSettingsDto;
import com.example.timecheck.service.mapper.TrackSettingsMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackSettingService {
    private final TrackSettingsRepository trackSettingsRepository;
    private final TrackSettingsMapper trackSettingsMapper;

    public TrackSettingService(TrackSettingsRepository trackSettingsRepository, TrackSettingsMapper trackSettingsMapper) {
        this.trackSettingsRepository = trackSettingsRepository;
        this.trackSettingsMapper = trackSettingsMapper;
    }

    public TrackSettingsDto create(TrackSettingsDto trackSettingsDto) {
        TrackSettings trackSettings = trackSettingsMapper.toEntity(trackSettingsDto);
        trackSettingsRepository.save(trackSettings);
        return trackSettingsMapper.toDto(trackSettings);
    }

    public TrackSettingsDto update(TrackSettingsDto trackSettingsDto) {
        if (trackSettingsDto.getId() == null || !trackSettingsRepository.existsById(trackSettingsDto.getId())) {
            throw new RuntimeException("TrackSettings not found for update");
        }
        TrackSettings trackSettings = trackSettingsMapper.toEntity(trackSettingsDto);
        trackSettingsRepository.save(trackSettings);
        return trackSettingsMapper.toDto(trackSettings);
    }

    public List<TrackSettingsDto> getAll() {
        return trackSettingsRepository
                .findAll()
                .stream()
                .map(trackSettingsMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TrackSettingsDto> findByTrackSettingsStatus() {
        return trackSettingsRepository
                .findByTrackSettingsStatusList(TrackSettingsStatus.ACTIVE)
                .stream()
                .map(trackSettingsMapper::toDto)
                .collect(Collectors.toList());
    }

    public TrackSettingsDto getId(Long id) {
        TrackSettings trackSettings = trackSettingsRepository.findById(id).orElseThrow(() -> new RuntimeException("TrackSettings not found"));
        return trackSettingsMapper.toDto(trackSettings);
    }

    public TrackSettingsDto delete(Long id) {
        TrackSettings trackSettings = trackSettingsRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("TrackSettings not found"));
        trackSettings.setTrackSettingsStatus(TrackSettingsStatus.DELETED);
        trackSettingsRepository.save(trackSettings);
        return trackSettingsMapper.toDto(trackSettings);
    }

    public void deleted(Long id) {
        TrackSettings trackSettings = trackSettingsRepository.findById(id).orElseThrow(() -> new RuntimeException("TrackSettings not found"));
        trackSettingsRepository.delete(trackSettings);
    }
}