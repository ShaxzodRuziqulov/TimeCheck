package com.example.timecheck.service.dto;


import com.example.timecheck.entity.enumirated.JobStatus;
import com.example.timecheck.entity.enumirated.PositionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {
    private Long id;
    private PositionStatus positionStatus;
    private JobStatus jobStatus;
    private Long departmentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
