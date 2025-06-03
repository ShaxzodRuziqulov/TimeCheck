package com.example.timecheck.service;

import com.example.timecheck.entity.TimeTrack;
import com.example.timecheck.entity.TrackSettings;
import com.example.timecheck.entity.User;
import com.example.timecheck.entity.enumirated.TrackSettingsStatus;
import com.example.timecheck.repository.TimeTrackRepository;
import com.example.timecheck.repository.TrackSettingsRepository;
import com.example.timecheck.repository.UserRepository;
import com.example.timecheck.responce.TimeTrackFilterRequest;
import com.example.timecheck.responce.TimeTrackSpecification;
import com.example.timecheck.service.dto.TimeTrackDto;
import com.example.timecheck.service.dto.TimeTrackUserDto;
import com.example.timecheck.service.dto.WorkSummaryDto;
import com.example.timecheck.service.mapper.TimeTrackMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Foydalanuvchi aniqlanmadi");
        }

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"))
                .getId();
    }


    public TimeTrackDto startTimeTrack() {
        ZoneId zone = ZoneId.of("Asia/Tashkent");
        LocalDate today = LocalDate.now(zone);
        LocalTime now = LocalTime.now(zone);
        Long currentUserId = getCurrentUserId();

        TimeTrack track = timeTrackRepository.findByUserIdAndDate(currentUserId, today)
                .orElseGet(() -> createNewTrack(currentUserId, today));

        if (track.getStartTime() != null) {
            throw new IllegalStateException("Siz bugun ishni allaqachon boshlagansiz.");
        }

        TrackSettings settings = getActiveSettings();
        track.setStartTime(now.isBefore(settings.getFromTime()) ? settings.getFromTime() : now);

        return timeTrackMapper.toDto(timeTrackRepository.save(track));
    }

    public Page<TimeTrack> getPaginatedFilteredTimeTracks(TimeTrackFilterRequest filterDto, Pageable pageable) {
        Specification<TimeTrack> spec = Specification
                .where(TimeTrackSpecification.hasDepartmentId(filterDto.getDepartmentId()))
                .and(TimeTrackSpecification.dateBetween(filterDto.getFromDate(), filterDto.getToDate()));

        return timeTrackRepository.findAll(spec, pageable);
    }

    private TimeTrack createNewTrack(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Foydalanuvchi topilmadi"));

        TimeTrack newTrack = new TimeTrack();
        newTrack.setUser(user);
        newTrack.setDate(date);
        return newTrack;
    }

    private TrackSettings getActiveSettings() {
        return trackSettingsRepository.findByTrackSettingsStatus(TrackSettingsStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("Faol ish sozlamalari topilmadi"));
    }

    public TimeTrackDto getWriteReason(TimeTrackDto timeTrackDto) {
        ZoneId tashkentZone = ZoneId.of("Asia/Tashkent");
        LocalDate today = LocalDate.now(tashkentZone);

        LocalDate dateToSave = timeTrackDto.getDate() != null ? timeTrackDto.getDate() : today;
        Long userId = timeTrackDto.getUserId();

        boolean alreadyExists = timeTrackRepository.existsByUserIdAndDate(userId, dateToSave);

        if (alreadyExists) {
            throw new IllegalStateException("Bugungi kun uchun yozuv allaqachon mavjud!");
        }

        TimeTrack result = timeTrackMapper.toEntity(timeTrackDto);
        result.setDelayReason(timeTrackDto.getDelayReason());
        result.setDate(dateToSave);

        timeTrackRepository.save(result);
        return timeTrackMapper.toDto(result);
    }

    public TimeTrackDto completeTimeTrack() {
        ZoneId tashkentZone = ZoneId.of("Asia/Tashkent");
        LocalDate today = LocalDate.now(tashkentZone);

        TimeTrack timeTrack = timeTrackRepository
                .findByUserIdAndDateAndEndTimeIsNull(this.getCurrentUserId(), today)
                .orElseThrow(() -> new EntityNotFoundException("TimeTrack not found"));

        if (timeTrack.getStartTime() == null) {
            throw new IllegalStateException("Cannot complete work before starting");
        }

        LocalTime endTime = LocalTime.now(tashkentZone);
        timeTrack.setEndTime(endTime);
        timeTrackRepository.save(timeTrack);

        return timeTrackMapper.toDto(timeTrack);
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
        Long userId = getCurrentUserId();
        return timeTrackRepository.getAllWithUserInfoByUserId(userId);
    }

    public Page<TimeTrack> getPaginatedTimeTracks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return timeTrackRepository.findAll(pageable);
    }
}
