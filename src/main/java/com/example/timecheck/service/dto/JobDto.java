package com.example.timecheck.service.dto;


import com.example.timecheck.entity.enumirated.JobStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobDto {
    private Long id;
    private Long positionId;
    private JobStatus jobStatus;
    private Long departmentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
