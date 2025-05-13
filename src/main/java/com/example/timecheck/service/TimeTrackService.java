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
import java.time.LocalDateTime;
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

    public TimeTrackDto create(TimeTrackDto timeTrackDto) {
        LocalTime now = LocalTime.now();

        LocalDate today = LocalDate.now();
        LocalTime startOfDay = LocalTime.from(today.atStartOfDay());
        LocalTime endOfDay = LocalTime.from(today.atTime(LocalTime.MAX));


        boolean isExists = timeTrackRepository.existsByUserIdAndStartTimeBetween(timeTrackDto.getUserId(), startOfDay, endOfDay);

        if (isExists) {
            throw new IllegalStateException("User already has a time track for today");
        }

        TrackSettings settings = trackSettingsRepository.findByTrackSettingsStatus(TrackSettingsStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("No active track settings found"));

        LocalTime fromTime = settings.getFromTime();
        LocalTime toTime = settings.getToTime();

        TimeTrack timeTrack = timeTrackMapper.toEntity(timeTrackDto);
        timeTrack.setUser(userRepository.findById(timeTrackDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found")));

        if (now.isBefore(fromTime)) {
            timeTrack.setStartTime(fromTime);
        } else if (!now.isAfter(toTime)) {
            timeTrack.setStartTime(now);
        } else {
            timeTrack.setStartTime(now);
        }

        timeTrackRepository.save(timeTrack);
        return timeTrackMapper.toDto(timeTrack);

    }

    public TimeTrackDto completeTimeTrack(Long userId) {
        TimeTrack timeTrack = timeTrackRepository
                .findByUserIdAndEndTimeIsNull(userId)
                .orElseThrow(() -> new EntityNotFoundException("TimeTrack not found"));

        timeTrack.setEndTime(LocalTime.now());
        timeTrackRepository.save(timeTrack);

        return timeTrackMapper.toDto(timeTrack);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void completeUnfinishedTimeTrack() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);

        List<TimeTrack> unfinishedTracks = timeTrackRepository.findAllByCreatedAtBetweenAndEndTimeIsNull(todayStart, todayEnd);

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
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        Optional<TimeTrack> optionalTrack = timeTrackRepository
                .findFirstByUserIdAndCreatedAtBetweenOrderByStartTimeAsc(userId, startOfDay, endOfDay);

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
