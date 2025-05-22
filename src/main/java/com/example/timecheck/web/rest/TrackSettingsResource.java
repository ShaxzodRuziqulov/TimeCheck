package com.example.timecheck.web.rest;

import com.example.timecheck.service.TrackSettingService;
import com.example.timecheck.service.dto.TrackSettingsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/track-settings")
public class TrackSettingsResource {

    private final TrackSettingService trackSettingService;

    public TrackSettingsResource(TrackSettingService trackSettingService) {
        this.trackSettingService = trackSettingService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTrackSettings(@RequestBody TrackSettingsDto trackSettingsDto) throws URISyntaxException {
        TrackSettingsDto createdTrackSettings = trackSettingService.create(trackSettingsDto);
        return ResponseEntity.created(new URI("/api/admin/track-settings/create/" + createdTrackSettings.getId())).body(createdTrackSettings);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTrackSettings(@RequestBody TrackSettingsDto trackSettingsDto, @PathVariable Long id) throws URISyntaxException {
        if (trackSettingsDto.getId() == null || !trackSettingsDto.getId().equals(id)) {
            return ResponseEntity.badRequest().body("Invalid or mismatched ID");
        }

        TrackSettingsDto result = trackSettingService.update(trackSettingsDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<?> allTrackSettings() {
        List<TrackSettingsDto> result = trackSettingService.getAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/allActive")
    public ResponseEntity<?> findByTrackSettingsStatus() {
        List<TrackSettingsDto> result = trackSettingService.findByTrackSettingsStatus();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        TrackSettingsDto result = trackSettingService.getId(id);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTrackSettings(@PathVariable Long id) {
        TrackSettingsDto result = trackSettingService.delete(id);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/deleted/{id}")
    public ResponseEntity<?> deletedTrackSettings(@PathVariable Long id) {
        trackSettingService.deleted(id);
        return ResponseEntity.ok().body("deleted");
    }
}
