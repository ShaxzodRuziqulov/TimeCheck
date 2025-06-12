package com.example.timecheck.service;

import com.example.timecheck.entity.Position;
import com.example.timecheck.repository.PositionRepository;
import com.example.timecheck.service.dto.PositionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {
    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public Position createPosition(PositionDto positionDto) {
        Position position = new Position();
        position.setId(positionDto.getId());
        position.setName(positionDto.getName());

        return positionRepository.save(position);
    }

    public List<Position> findAll() {
        return positionRepository.findAll();
    }

    public void deletePosition(Long id) {
        Position position = positionRepository.getReferenceById(id);
        positionRepository.delete(position);
    }
}
