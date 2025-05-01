package com.example.timecheck.service.dto;

import com.example.timecheck.entity.enumirated.DepartmentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepartmentDto {
    private Long id;
    private String name;
    private DepartmentStatus departmentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
