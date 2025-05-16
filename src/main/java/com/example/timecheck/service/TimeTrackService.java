package com.example.timecheck.service;

import com.example.timecheck.entity.TimeTrack;
import com.example.timecheck.entity.TrackSettings;
import com.example.timecheck.entity.User;
import com.example.timecheck.entity.enumirated.TrackSettingsStatus;
import com.example.timecheck.repository.TimeTrackRepository;
import com.example.timecheck.repository.TrackSettingsRepository;
import com.example.timecheck.repository.UserRepository;
import com.example.timecheck.service.dto.TimeTrackDto;
import com.example.timecheck.service.dto.TimeTrackUserDto;
import com.example.timecheck.service.dto.WorkSummaryDto;
import com.example.timecheck.service.mapper.TimeTrackMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeTrackService {
    private final TimeTrackMapper timeTrackMapper;
    private final TimeTrackRepository timeTrackRepository;
    private final UserRepository userRepository;
    private final TrackSettingsRepository trackSettingsRepository;


    public TimeTrackDto startTimeTrack(TimeTrackDto timeTrackDto) {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        Optional<TimeTrack> optionalTimeTrack = timeTrackRepository
                .findByUserIdAndDate(timeTrackDto.getUserId(), today);

        if (optionalTimeTrack.isPresent()) {
            TimeTrack existingTrack = optionalTimeTrack.get();
            if (existingTrack.getStartTime() != null) {
                throw new IllegalStateException("User has already started work today");
            }
            return saveStartTime(existingTrack, now);
        }

        TrackSettings settings = trackSettingsRepository
                .findByTrackSettingsStatus(TrackSettingsStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("No active track settings found"));

        TimeTrack newTrack = timeTrackMapper.toEntity(timeTrackDto);
        newTrack.setUser(userRepository.findById(timeTrackDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
        newTrack.setDate(today);

        return saveStartTime(newTrack, now, settings);
    }

    private TimeTrackDto saveStartTime(TimeTrack timeTrack, LocalTime now, TrackSettings settings) {
        LocalTime fromTime = settings.getFromTime();

        timeTrack.setStartTime(now.isBefore(fromTime) ? fromTime : now);
        timeTrackRepository.save(timeTrack);

        return timeTrackMapper.toDto(timeTrack);
    }

    private TimeTrackDto saveStartTime(TimeTrack timeTrack, LocalTime now) {
        TrackSettings settings = trackSettingsRepository
                .findByTrackSettingsStatus(TrackSettingsStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("No active track settings found"));

        return saveStartTime(timeTrack, now, settings);
    }


    public TimeTrackDto getWriteReason(TimeTrackDto timeTrackDto) {
        LocalDate today = LocalDate.now();

        boolean isStarted = timeTrackRepository.existsByUserIdAndDateAndStartTimeIsNotNull(timeTrackDto.getUserId(), today);
        if (isStarted) {
            throw new IllegalStateException("User has already started work today");
        }
        TimeTrack result = timeTrackMapper.toEntity(timeTrackDto);
        result.setDelayReason(timeTrackDto.getDelayReason());

        LocalDate dateToSave = timeTrackDto.getDate() != null ? timeTrackDto.getDate() : today;

        result.setDate(dateToSave);
        timeTrackRepository.save(result);
        return timeTrackMapper.toDto(result);
    }

    public TimeTrackDto completeTimeTrack(Long userId) {
        LocalDate today = LocalDate.now();

        TimeTrack timeTrack = timeTrackRepository
                .findByUserIdAndDateAndEndTimeIsNull(userId, today)
                .orElseThrow(() -> new EntityNotFoundException("TimeTrack not found"));

        if (timeTrack.getStartTime() == null) {
            throw new IllegalStateException("Cannot complete work before starting");
        }

        timeTrack.setEndTime(LocalTime.now());
        timeTrackRepository.save(timeTrack);

        return timeTrackMapper.toDto(timeTrack);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void completeUnfinishedTimeTrack() {
        LocalDate todayStart = LocalDate.from(LocalDate.now().atStartOfDay());
        LocalDate todayEnd = LocalDate.from(LocalDate.now().atTime(LocalTime.MAX));

        List<TimeTrack> unfinishedTracks = timeTrackRepository.findAllByDateBetweenAndEndTimeIsNull(todayStart, todayEnd);

        if (unfinishedTracks.isEmpty()) {
            return;
        }
        for (TimeTrack track : unfinishedTracks) {
            track.setEndTime(LocalTime.now());
            track.setEndReason("Tugatish tugmasi bosilmagan");
        }
        timeTrackRepository.saveAll(unfinishedTracks);
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

    public WorkSummaryDto getTodayWorkSummary(Long userId) {
        LocalDate startOfDay = LocalDate.from(LocalDate.now().atStartOfDay());
        LocalDate endOfDay = LocalDate.from(LocalDate.now().atTime(LocalTime.MAX));

        Optional<TimeTrack> optionalTrack = timeTrackRepository
                .findFirstByUserIdAndDateBetweenOrderByStartTimeAsc(userId, startOfDay, endOfDay);

        if (optionalTrack.isEmpty()) {
            throw new RuntimeException("No time track found for user today");
        }

        TimeTrack track = optionalTrack.get();
        LocalTime startTime = track.getStartTime();
        LocalTime endTime = track.getEndTime();

        WorkSummaryDto summary = new WorkSummaryDto();
        summary.setStartTime(startTime);
        summary.setEndTime(endTime);

        if (startTime != null && endTime != null) {
            Duration duration = Duration.between(startTime, endTime);
            summary.setWorkedHours(String.format("%02d:%02d", duration.toHours(), duration.toMinutesPart()));
            summary.setCanLeave(true);
        } else {
            LocalTime now = LocalTime.now();
            boolean canLeave = false;
            if (startTime != null) {
                canLeave = now.isAfter(startTime.plusHours(9));
            }
            summary.setCanLeave(canLeave);
        }

        return summary;
    }

    public List<TimeTrackUserDto> getAllWithUserDetails() {
        return timeTrackRepository.getAllWithUserInfo();
    }

}
