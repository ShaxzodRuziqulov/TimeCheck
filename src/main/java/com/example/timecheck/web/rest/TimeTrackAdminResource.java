package com.example.timecheck.web.rest;

import com.example.timecheck.entity.TimeTrack;
import com.example.timecheck.responce.PageResponse;
import com.example.timecheck.responce.TimeTrackFilterRequest;
import com.example.timecheck.service.TimeTrackService;
import com.example.timecheck.service.dto.TimeTrackDto;
import com.example.timecheck.service.dto.WorkSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/admin/time-track")
public class TimeTrackAdminResource {
    private final TimeTrackService timeTrackService;

    public TimeTrackAdminResource(TimeTrackService timeTrackService) {
        this.timeTrackService = timeTrackService;
    }

    @PostMapping("/reason")
    public ResponseEntity<?> createReason(@RequestBody TimeTrackDto timeTrackDto) {
        TimeTrackDto result = timeTrackService.getWriteReason(timeTrackDto);
        return ResponseEntity.created(URI.create("/api/admin/time-track/reason/" + result.getId())).body(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        timeTrackService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<TimeTrack>> getAllFiltered(
            @RequestBody TimeTrackFilterRequest filterDto,
            @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<TimeTrack> result = timeTrackService.getPaginatedFilteredTimeTracks(filterDto, pageable);
        return ResponseEntity.ok(new PageResponse<>(result));
    }

    @GetMapping
    public ResponseEntity<Page<TimeTrack>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(timeTrackService.getPaginatedTimeTracks(page, size));
    }

    @GetMapping("/check/{userId}")
    public ResponseEntity<?> checkIfUserCanLeave(@PathVariable Long userId) {
        WorkSummaryDto result = timeTrackService.getTodayWorkSummary(userId);
        return ResponseEntity.ok().body(result);
    }
}
