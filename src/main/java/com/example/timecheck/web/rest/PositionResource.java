package com.example.timecheck.web.rest;

import com.example.timecheck.entity.Position;
import com.example.timecheck.service.PositionService;
import com.example.timecheck.service.dto.PositionDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/admin/position")
public class PositionResource {
    private final PositionService positionService;

    public PositionResource(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPosition(@Valid @RequestBody PositionDto positionDto) throws URISyntaxException {
        Position position = positionService.createPosition(positionDto);
        return ResponseEntity.created(new URI("/api/position/" + positionDto.getId())).body(position);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePosition(@PathVariable Long id) {
        positionService.deletePosition(id);
        return ResponseEntity.ok().build();
    }
}
