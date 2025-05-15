package com.example.timecheck.web.rest;

import com.example.timecheck.service.TimeTrackService;
import com.example.timecheck.service.dto.TimeTrackDto;
import com.example.timecheck.service.dto.TimeTrackUserDto;
import com.example.timecheck.service.dto.WorkSummaryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/user/time-track")
public class TimeTrackResource {
    private final TimeTrackService timeTrackService;


    public TimeTrackResource(TimeTrackService timeTrackService) {
        this.timeTrackService = timeTrackService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TimeTrackDto timeTrackDto) {
        TimeTrackDto result = timeTrackService.startTimeTrack(timeTrackDto);
        return ResponseEntity.created(URI.create("/api/admin/time-track/create/" + result.getId())).body(result);
    }

    @PostMapping("/reason")
    public ResponseEntity<?> createReason(@RequestBody TimeTrackDto timeTrackDto) {
        TimeTrackDto result = timeTrackService.getWriteReason(timeTrackDto);
        return ResponseEntity.created(URI.create("/api/admin/time-track/reason/" + result.getId())).body(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody TimeTrackDto timeTrackDto, @PathVariable Long id) {
        if (timeTrackDto.getId() == 0 || !timeTrackDto.getId().equals(id)) {
            return ResponseEntity.badRequest().body("Invalid or mismatched ID");
        }
        TimeTrackDto result = timeTrackService.update(timeTrackDto);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/complete/{userId}")
    public ResponseEntity<?> completeTimeTrack(@PathVariable Long userId) {
        TimeTrackDto result = timeTrackService.completeTimeTrack(userId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<TimeTrackDto> result = timeTrackService.findAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        TimeTrackDto result = timeTrackService.findById(id);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        timeTrackService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check/{userId}")
    public ResponseEntity<?> checkIfUserCanLeave(@PathVariable Long userId) {
        WorkSummaryDto result = timeTrackService.getTodayWorkSummary(userId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<TimeTrackUserDto> result = timeTrackService.getAllWithUserDetails();
        return ResponseEntity.ok().body(result);
    }
}